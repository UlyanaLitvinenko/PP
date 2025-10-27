import java.io.*;
import java.util.*;

public class Main {
    static class Student {
        long num;
        String name;
        int group;
        double grade;

        Student(long num, String name, int group, double grade) {
            this.num = num;
            this.name = name;
            this.group = group;
            this.grade = grade;
        }

        public boolean equals(Object o) {
            return o instanceof Student s && num == s.num;
        }

        public int hashCode() {
            return Long.hashCode(num);
        }

        public String toString() {
            return num + " " + name + " " + group + " " + grade;
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Файл 1: ");
        String file1 = scanner.nextLine();
        System.out.print("Файл 2: ");
        String file2 = scanner.nextLine();
        System.out.print("Результат: ");
        String resultFile = scanner.nextLine();

        List<Student> list1 = read(file1);
        List<Student> list2 = read(file2);
        Set<Student> result = new LinkedHashSet<>();

        System.out.println("1 - Объединение");
        System.out.println("2 - Пересечение");
        System.out.println("3 - Разность");
        int choice = scanner.nextInt();

        if (choice == 1) {
            result.addAll(list1);
            result.addAll(list2);
        } else if (choice == 2) {
            for (Student s : list1)
                if (list2.contains(s)) result.add(s);
        } else if (choice == 3) {
            for (Student s : list1)
                if (!list2.contains(s)) result.add(s);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile));
        for (Student s : result) writer.write(s + "\n");
        writer.close();
        System.out.println("Готово!");
    }

    static List<Student> read(String filename) throws IOException {
        List<Student> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] p = line.trim().split("\\s+");
            if (p.length == 4)
                list.add(new Student(Long.parseLong(p[0]), p[1], Integer.parseInt(p[2]), Double.parseDouble(p[3])));
        }
        reader.close();
        return list;
    }
}
