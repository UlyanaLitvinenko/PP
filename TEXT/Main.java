import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ширину строки для форматирования текста: ");
        int width = scanner.nextInt();

        TextFormatter formatter = new TextFormatter("input.txt", "output.txt", width);
        try {
            formatter.formatText();
            System.out.println("Форматирование завершено. Результат записан в output.txt");
        } catch (IOException e) {
            System.out.println("Ошибка при обработке файла: " + e.getMessage());
        }
    }
}
