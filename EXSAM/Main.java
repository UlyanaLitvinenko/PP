import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Transaction {
    private String date;
    private String description;
    private double amount;
    private String type;

    public Transaction(String date, String description, double amount, String type) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }
}

public class Main {
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("Финансовый калькулятор");

        while (running) {
            showMenu();
            int choice = getInt("Выбор: ");

            switch (choice) {
                case 1 -> addTransaction();
                case 2 -> showAllTransactions();
                case 3 -> calculateBalance();
                case 4 -> showTransactionsByType();
                case 5 -> running = false;
                default -> System.out.println("Неверный пункт меню");
            }
        }

        System.out.println("Завершение работы");
    }

    private static void showMenu() {
        System.out.println("\nМеню:");
        System.out.println("1. Добавить транзакцию");
        System.out.println("2. Показать все транзакции");
        System.out.println("3. Текущий баланс");
        System.out.println("4. Транзакции по типу");
        System.out.println("5. Выход");
    }

    private static void addTransaction() {
        System.out.println("\nНовая транзакция");

        String date = getValidDate("Дата (дд.мм.гггг): ");

        System.out.print("Описание: ");
        String description = scanner.nextLine();

        double amount = getDouble("Сумма: ");
        String type = getTransactionType();

        transactions.add(new Transaction(date, description, amount, type));
        System.out.println("Транзакция добавлена");
    }

    private static void showAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("Нет транзакций");
            return;
        }

        System.out.println("\nСписок транзакций:");
        System.out.printf("%-12s %-20s %-10s %-8s%n",
                "Дата", "Описание", "Сумма", "Тип");

        for (Transaction t : transactions) {
            System.out.printf("%-12s %-20s %-10.2f %-8s%n",
                    t.getDate(),
                    t.getDescription(),
                    t.getAmount(),
                    t.getType());
        }
    }

    private static void calculateBalance() {
        double income = 0;
        double expense = 0;

        for (Transaction t : transactions) {
            if (t.getType().equals("Доход")) {
                income += t.getAmount();
            } else {
                expense += t.getAmount();
            }
        }

        System.out.println("\nФинансовый отчет:");
        System.out.println("Доходы: " + income);
        System.out.println("Расходы: " + expense);
        System.out.println("Баланс: " + (income - expense));
    }

    private static void showTransactionsByType() {
        String type = getTransactionType();
        boolean found = false;

        System.out.println("\nТранзакции (" + type + "):");
        System.out.printf("%-12s %-20s %-10s%n",
                "Дата", "Описание", "Сумма");

        for (Transaction t : transactions) {
            if (t.getType().equals(type)) {
                System.out.printf("%-12s %-20s %-10.2f%n",
                        t.getDate(),
                        t.getDescription(),
                        t.getAmount());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Транзакций не найдено");
        }
    }

    private static String getValidDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                LocalDate.parse(input, formatter);
                return input;
            } catch (Exception e) {
                System.out.println("Ошибка: введите дату в формате дд.мм.гггг");
            }
        }
    }

    private static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Введите целое число");
            }
        }
    }

    private static double getDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Введите число");
            }
        }
    }

    private static String getTransactionType() {
        while (true) {
            System.out.print("Тип (доход/расход): ");
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("доход")) return "Доход";
            if (input.equals("расход")) return "Расход";
            System.out.println("Введите доход или расход");
        }
    }
}
