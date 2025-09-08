import java.util.Scanner;

import static java.lang.Math.asin;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Введите значение x (от -1 до 1): ");
            double x = scanner.nextDouble();

            if (x <=-1 || x >= 1) {
                System.out.println("Ошибка: x должен быть в диапазоне (-1, 1)");
                return;
            }

            System.out.print("Введите значение k (натуральное число): ");
            int k = scanner.nextInt();

            if (k <= 0) {
                System.out.println("Ошибка, так как k должен быть натуральным числом");
                return;
            }

            double variant1 =ArcsinTaylor.countArcsin(x, k);
            double variant2 = asin(x);

            System.out.println("Приближённое значение: " + variant1);
            System.out.println("Точное значение: " + variant2);


        } catch (Exception e) {
            System.out.println("Ошибка! Введите корректные значения.");
        } finally {
            scanner.close();
        }
    }
}


