import java.text.*;
import java.util.*;

// ------------------------------
// Шаблон Singleton: глобальная конфигурация
// ------------------------------
class AppConfig {
    private static AppConfig instance;
    private String language = "ru";

    private AppConfig() {}

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String lang) {
        this.language = lang;
    }
}

// ------------------------------
// Шаблон Builder: создание клиента
// ------------------------------
class Client {
    private String name;
    private Locale locale;
    private double balance;

    private Client(Builder builder) {
        this.name = builder.name;
        this.locale = builder.locale;
        this.balance = builder.balance;
    }

    public String getName() {
        return name;
    }

    public Locale getLocale() {
        return locale;
    }

    public double getBalance() {
        return balance;
    }

    // Вложенный класс Builder
    public static class Builder {
        private String name;
        private Locale locale;
        private double balance;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder setBalance(double balance) {
            this.balance = balance;
            return this;
        }

        public Client build() {
            return new Client(this);
        }
    }
}

// ------------------------------
// Шаблон Decorator: расширение уведомлений
// ------------------------------
interface Notifier {
    void send(String message);
}

// Базовая реализация
class EmailNotifier implements Notifier {
    public void send(String message) {
        System.out.println("📧 Email: " + message);
    }
}

// Декоратор
class NotifierDecorator implements Notifier {
    protected Notifier wrappee;

    public NotifierDecorator(Notifier notifier) {
        this.wrappee = notifier;
    }

    public void send(String message) {
        wrappee.send(message);
    }
}

// Расширение: Telegram
class TelegramNotifier extends NotifierDecorator {
    public TelegramNotifier(Notifier notifier) {
        super(notifier);
    }

    public void send(String message) {
        super.send(message);
        System.out.println("📲 Telegram: " + message);
    }
}

// ------------------------------
// Основной класс проекта
// ------------------------------
public class Main {
    // Локализованные сообщения прямо в коде
    static Map<String, Map<String, String>> messages = new HashMap<>();

    static {
        messages.put("en", Map.of(
                "welcome", "Welcome, {0}!",
                "balance", "Your balance is {0}",
                "date", "Today is {0}"
        ));
        messages.put("ru", Map.of(
                "welcome", "Добро пожаловать, {0}!",
                "balance", "Ваш баланс: {0}",
                "date", "Сегодня: {0}"
        ));
        messages.put("fr", Map.of(
                "welcome", "Bienvenue, {0}!",
                "balance", "Votre solde est {0}",
                "date", "Nous sommes le {0}"
        ));
        messages.put("ka", Map.of(
                "welcome", "მოგესალმებით, {0}!",
                "balance", "თქვენი ბალანსია {0}",
                "date", "დღეს არის {0}"
        ));
        messages.put("zh", Map.of(
                "welcome", "欢迎, {0}！",
                "balance", "您的余额是 {0}",
                "date", "今天是 {0}"
        ));
    }

    public static void main(String[] args) {
        // Настройка через Singleton
        AppConfig config = AppConfig.getInstance();
        config.setLanguage("ru"); // можно поменять на "en", "fr", "ka", "zh"// Создание клиентов через Builder
        List<Client> clients = Arrays.asList(
                new Client.Builder().setName("Ульяна").setLocale(new Locale("ru", "BY")).setBalance(12345.67).build(),
                new Client.Builder().setName("John").setLocale(new Locale("en", "US")).setBalance(9876.54).build(),
                new Client.Builder().setName("Pierre").setLocale(new Locale("fr", "FR")).setBalance(4321.00).build(),
                new Client.Builder().setName("Nino").setLocale(new Locale("ka", "GE")).setBalance(5555.55).build(),
                new Client.Builder().setName("Li Wei").setLocale(new Locale("zh", "CN")).setBalance(8888.88).build()
        );

        Date today = new Date();

        for (Client client : clients) {
            String lang = client.getLocale().getLanguage();
            Map<String, String> msg = messages.getOrDefault(lang, messages.get("en"));

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(client.getLocale());
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, client.getLocale());

            String welcome = MessageFormat.format(msg.get("welcome"), client.getName());
            String balance = MessageFormat.format(msg.get("balance"), currencyFormat.format(client.getBalance()));
            String date = MessageFormat.format(msg.get("date"), dateFormat.format(today));

            System.out.println("-----");
            System.out.println(welcome);
            System.out.println(balance);
            System.out.println(date);

            // Уведомление через Decorator
            Notifier notifier = new TelegramNotifier(new EmailNotifier());
            notifier.send(welcome + " " + balance);
        }
    }
}