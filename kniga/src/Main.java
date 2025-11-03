import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<GradeBook> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String lastName = parts[0];
                String firstName = parts[1];
                String middleName = parts[2];
                int course = Integer.parseInt(parts[3]);
                String group = parts[4];

                GradeBook gb = new GradeBook(lastName, firstName, middleName, course, group);

                for (int i = 5; i + 3 < parts.length; i += 4) {
                    int sessionNum = Integer.parseInt(parts[i]);
                    String subject = parts[i + 1];
                    boolean passed = Boolean.parseBoolean(parts[i + 2]);
                    int mark = Integer.parseInt(parts[i + 3]);
                    gb.addSession(sessionNum, subject, passed, mark);
                }

                students.add(gb);
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            for (GradeBook gb : students) {
                if (gb.isExcellent()) {
                    writer.write(gb.toString());
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи файла: " + e.getMessage());
        }
    }

    static class GradeBook {
        String lastName;
        String firstName;
        String middleName;
        int course;
        String group;
        List<Session> sessions = new ArrayList<>();

        GradeBook(String lastName, String firstName, String middleName, int course, String group) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.middleName = middleName;
            this.course = course;
            this.group = group;
        }

        void addSession(int number, String subject, boolean passed, int mark) {
            sessions.add(new Session(number, subject, passed, mark));
        }

        boolean isExcellent() {
            for (Session s : sessions) {
                if (!s.passed || s.mark < 9) return false;
            }
            return true;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Session s : sessions) {
                sb.append(lastName).append(" ").append(firstName).append(" ").append(middleName).append(", ");
                sb.append("Курс: ").append(course).append(", Группа: ").append(group).append(", ");
                sb.append("Сессия №").append(s.number).append(", Предмет: ").append(s.subject).append(", ");
                sb.append("Оценка: ").append(s.mark).append("\n");
            }
            return sb.toString();
        }

        class Session {
            int number;
            String subject;
            boolean passed;
            int mark;

            Session(int number, String subject, boolean passed, int mark) {
                this.number = number;
                this.subject = subject;
                this.passed = passed;
                this.mark = mark;
            }
        }
    }
}