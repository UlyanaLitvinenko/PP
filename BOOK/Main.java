package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<GradeBook> students = new ArrayList<>();

        // Чтение из файла
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

        // Работа с JSON
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            List<GradeBook> excellentStudents = new ArrayList<>();
            for (GradeBook gb : students) {
                if (gb.isExcellent()) {
                    excellentStudents.add(gb);
                }
            }

            // Запись в JSON файл
            mapper.writeValue(new File("output.json"), excellentStudents);
            System.out.println("Данные успешно записаны в output.json");

            // Также сохраняем в текстовый файл
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                for (GradeBook gb : excellentStudents) {
                    writer.write(gb.toString());
                    writer.write("\n");
                }
            }
            System.out.println("Данные успешно записаны в output.txt");

        } catch (IOException e) {
            System.out.println("Ошибка записи файла: " + e.getMessage());
        }
    }

    static class GradeBook {
        private String lastName;
        private String firstName;
        private String middleName;
        private int course;
        private String group;
        private List<Session> sessions = new ArrayList<>();

        public GradeBook(String lastName, String firstName, String middleName, int course, String group) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.middleName = middleName;
            this.course = course;
            this.group = group;
        }

        // Геттеры для JSON
        public String getLastName() { return lastName; }
        public String getFirstName() { return firstName; }
        public String getMiddleName() { return middleName; }
        public int getCourse() { return course; }
        public String getGroup() { return group; }
        public List<Session> getSessions() { return sessions; }

        // Сеттеры для Jackson (добавьте их)
        public void setLastName(String lastName) { this.lastName = lastName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public void setMiddleName(String middleName) { this.middleName = middleName; }
        public void setCourse(int course) { this.course = course; }
        public void setGroup(String group) { this.group = group; }
        public void setSessions(List<Session> sessions) { this.sessions = sessions; }

        public void addSession(int number, String subject, boolean passed, int mark) {
            sessions.add(new Session(number, subject, passed, mark));
        }

        // Убрали геттер isExcellent() чтобы его не было в JSON
        public boolean isExcellent() {
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

        static class Session {
            private int number;
            private String subject;
            private boolean passed;
            private int mark;

            public Session(int number, String subject, boolean passed, int mark) {
                this.number = number;
                this.subject = subject;
                this.passed = passed;
                this.mark = mark;
            }

            // Геттеры для JSON
            public int getNumber() { return number; }
            public String getSubject() { return subject; }
            public boolean isPassed() { return passed; }
            public int getMark() { return mark; }

            // Сеттеры для Jackson
            public void setNumber(int number) { this.number = number; }
            public void setSubject(String subject) { this.subject = subject; }
            public void setPassed(boolean passed) { this.passed = passed; }
            public void setMark(int mark) { this.mark = mark; }
        }
    }
}