package command;

import java.util.ArrayList;
import java.util.List;

public class CommandInvoker {
    private final List<Command> queue = new ArrayList<>();

    public void add(Command command) {
        queue.add(command);
    }

    public void runAll() {
        for (Command c : queue) {
            c.execute();
        }
        queue.clear();
    }
}
