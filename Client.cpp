#include <windows.h>
#include <iostream>
#include <string>

struct worker {
    int id;
    char fio[10];
    double hours;
};

enum Command { CMD_READ_BEGIN, CMD_READ_END, CMD_WRITE_LOCK, CMD_WRITE_SAVE, CMD_WRITE_UNLOCK, CMD_EXIT };
enum Result { RES_OK, RES_NOT_FOUND, RES_BUSY, RES_BAD_PROTOCOL };

struct Request {
    Command cmd;
    int     id;
    worker  rec;
    DWORD   pid;
};

struct Reply {
    Result  res;
    worker  rec;
};

static const wchar_t* PIPE_PATH = L"\\\\.\\pipe\\EmpServer";

bool exchange(HANDLE pipe, const Request& req, Reply& rep) {
    DWORD written = 0;
    if (!WriteFile(pipe, &req, sizeof(req), &written, nullptr) || written != sizeof(req)) {
        std::cerr << "[Error] Write failed, code=" << GetLastError() << "\n";
        return false;
    }
    DWORD readBytes = 0;
    if (!ReadFile(pipe, &rep, sizeof(rep), &readBytes, nullptr) || readBytes != sizeof(rep)) {
        std::cerr << "[Error] Read failed, code=" << GetLastError() << "\n";
        return false;
    }
    return true;
}

int main() {
    HANDLE pipe = INVALID_HANDLE_VALUE;

    std::cout << "=== Worker Client ===\n";
    std::cout << "Connecting...\n";

    while (true) {
        pipe = CreateFileW(PIPE_PATH, GENERIC_READ | GENERIC_WRITE, 0, nullptr, OPEN_EXISTING, 0, nullptr);
        if (pipe != INVALID_HANDLE_VALUE) break;

        if (GetLastError() != ERROR_PIPE_BUSY) {
            std::cerr << "[Error] Pipe open failed, code=" << GetLastError() << "\n";
            return 1;
        }
        if (!WaitNamedPipeW(PIPE_PATH, 5000)) {
            std::cerr << "[Error] WaitNamedPipe timeout.\n";
            return 1;
        }
    }

    DWORD mode = PIPE_READMODE_MESSAGE;
    SetNamedPipeHandleState(pipe, &mode, nullptr, nullptr);

    std::cout << "Connected!\n";
    std::cout << "Commands:\n";
    std::cout << "  read  - show worker record\n";
    std::cout << "  mod   - change worker record\n";
    std::cout << "  exit  - close client\n";
    std::cout << "-------------------------------\n";

    std::string cmd;
    while (true) {
        std::cout << "\n> ";
        if (!std::getline(std::cin, cmd)) break;

        if (cmd == "exit") {
            Request req{ CMD_EXIT, 0, {}, GetCurrentProcessId() };
            Reply rep{};
            exchange(pipe, req, rep);
            std::cout << "Disconnected.\n";
            break;
        }
        else if (cmd == "read") {
            int key;
            std::cout << "Enter ID: ";
            if (!(std::cin >> key)) {
                std::cin.clear();
                std::cin.ignore(10000, '\n');
                std::cout << "[Error] Wrong ID.\n";
                continue;
            }
            std::cin.ignore();

            Request req{ CMD_READ_BEGIN, key, {}, GetCurrentProcessId() };
            Reply rep{};
            if (!exchange(pipe, req, rep)) continue;

            if (rep.res == RES_OK) {
                std::cout << "Record:\n";
                std::cout << "  ID: " << rep.rec.id
                    << " | Name: " << rep.rec.fio
                    << " | Hours: " << rep.rec.hours << "\n";
                req.cmd = CMD_READ_END;
                exchange(pipe, req, rep);
            }
            else if (rep.res == RES_NOT_FOUND) {
                std::cout << "[Info] Not found.\n";
            }
            else if (rep.res == RES_BUSY) {
                std::cout << "[Info] Busy.\n";
            }
            else {
                std::cout << "[Error] Protocol error.\n";
            }
        }
        else if (cmd == "mod") {
            int key;
            std::cout << "Enter ID: ";
            if (!(std::cin >> key)) {
                std::cin.clear();
                std::cin.ignore(10000, '\n');
                std::cout << "[Error] Wrong ID.\n";
                continue;
            }
            std::cin.ignore();

            Request req{ CMD_WRITE_LOCK, key, {}, GetCurrentProcessId() };
            Reply rep{};
            if (!exchange(pipe, req, rep)) continue;

            if (rep.res != RES_OK) {
                std::cout << "[Error] Lock failed.\n";
                continue;
            }

            std::cout << "Current:\n";
            std::cout << "  ID: " << rep.rec.id
                << " | Name: " << rep.rec.fio
                << " | Hours: " << rep.rec.hours << "\n";

            worker w = rep.rec;
            std::string nm;
            std::cout << "New name: ";
            std::getline(std::cin, nm);
            for (size_t j = 0; j < sizeof(w.fio); ++j) {
                w.fio[j] = (j < nm.size()) ? nm[j] : '\0';
            }

            std::cout << "New hours: ";
            if (!(std::cin >> w.hours)) {
                std::cin.clear();
                std::cin.ignore(10000, '\n');
                std::cout << "[Error] Wrong hours.\n";
                req.cmd = CMD_WRITE_UNLOCK;
                exchange(pipe, req, rep);
                continue;
            }
            std::cin.ignore();

            req.cmd = CMD_WRITE_SAVE;
            req.rec = w;
            if (!exchange(pipe, req, rep)) {
                req.cmd = CMD_WRITE_UNLOCK;
                exchange(pipe, req, rep);
                continue;
            }

            if (rep.res == RES_OK) {
                std::cout << "[Success] Updated.\n";
            }
            else {
                std::cout << "[Error] Save failed.\n";
            }

            req.cmd = CMD_WRITE_UNLOCK;
            exchange(pipe, req, rep);
        }
        else {
            std::cout << "[Info] Unknown command.\n";
        }
    }

    CloseHandle(pipe);

    std::cout << "=== Client finished ===\n";
    std::cout << "Press Enter...";
    std::cin.get();

    return 0;
}
