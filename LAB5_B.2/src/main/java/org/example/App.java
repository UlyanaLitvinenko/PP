package org.example;
/*Во входном файле хранятся наименования некоторых объектов и их шифры.
Построить список C1, элементы которого содержат наименования и шифры объектов,
причём элементы списка должны быть упорядочены по возрастанию шифров. Затем «сжать»
список C1, удаляя дублирующие наименования объектов (оставляя только первый по шифру
для каждого уникального названия).*/
import java.io.*;
import java.util.*;

public class App {
    static class Item implements Comparable<Item> {
        String name;
        int code;

        Item(String name, int code) {
            this.name = name;
            this.code = code;
        }

        @Override
        public int compareTo(Item other) {
            return Integer.compare(this.code, other.code);
        }

        @Override
        public String toString() {
            return name + " " + code;
        }
    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();

        // 📂 Явный путь к файлу внутри src
        File inputFile = new File("src/input.txt");
        if (!inputFile.exists()) {
            System.out.println("Файл input.txt не найден в папке src.");
            return;
        }

        // 📥 Чтение данных
        try (Scanner scanner = new Scanner(inputFile)) {
            while (scanner.hasNext()) {
                String name = scanner.next();
                if (scanner.hasNextInt()) {
                    int code = scanner.nextInt();
                    items.add(new Item(name, code));
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return;
        }

        // 🔄 Обработка
        List<Item> result = process(items);

        // 📤 Запись результата
        try (PrintWriter writer = new PrintWriter("src/output.txt")) {
            for (Item item : result) {
                writer.println(item);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при записи файла: " + e.getMessage());
        }
    }

    // ✅ Метод для тестов и логики
    public static List<Item> process(List<Item> items) {
        Collections.sort(items);
        List<Item> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (Item item : items) {
            if (!seen.contains(item.name)) {
                result.add(item);
                seen.add(item.name);
            }
        }
        return result;
    }
}
