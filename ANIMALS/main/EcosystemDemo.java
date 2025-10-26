package main;

import animals.*;
import command.*;
import environment.Environment;
import habitat.*;
import registry.AnimalRegistry;
import state.*;
import feeding.FeedingStrategy;

import java.util.Scanner;

public class EcosystemDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AnimalGroup group = new AnimalGroup("Интерактивная группа");
        AnimalRegistry registry = AnimalRegistry.getInstance();
        Environment environment = new Environment();

        System.out.print("Сколько животных создать? ");
        int count = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < count; i++) {
            System.out.print("Введите тип животного (собака, тигр, дельфин, кот, мустанг): ");
            String type = scanner.nextLine().trim().toLowerCase();
            String internalType = switch (type) {
                case "собака" -> "dog";
                case "тигр" -> "tiger";
                case "дельфин" -> "dolphin";
                case "кот" -> "cat";
                case "мустанг" -> "mustang";
                default -> throw new IllegalArgumentException("Неизвестный тип животного: " + type);
            };

            System.out.print("Введите имя животного: ");
            String name = scanner.nextLine().trim();

            System.out.print("Выберите среду обитания (дикая, водная, домашняя): ");
            String habitatChoice = scanner.nextLine().trim().toLowerCase();
            HabitatStrategy habitat = switch (habitatChoice) {
                case "дикая" -> new WildHabitat();
                case "водная" -> new AquaticHabitat();
                case "домашняя" -> new DomesticHabitat();
                default -> new WildHabitat();
            };

            System.out.print("Выберите тип питания (хищник, травоядный, всеядный): ");
            String feedChoice = scanner.nextLine().trim().toLowerCase();
            FeedingStrategy.Type feedType = switch (feedChoice) {
                case "хищник" -> FeedingStrategy.Type.CARNIVORE;
                case "травоядный" -> FeedingStrategy.Type.HERBIVORE;
                case "всеядный" -> FeedingStrategy.Type.OMNIVORE;
                default -> FeedingStrategy.Type.OMNIVORE;
            };
            FeedingStrategy feeding = new FeedingStrategy(feedType);

            System.out.print("Введите метку (или оставьте пустым): ");
            String tag = scanner.nextLine().trim();
            if (tag.isBlank()) tag = null;

            System.out.print("Выберите состояние (голоден, игрив, сонный): ");
            String stateChoice = scanner.nextLine().trim().toLowerCase();
            AnimalState state = switch (stateChoice) {
                case "голоден" -> new HungryState();
                case "игрив" -> new PlayfulState();
                case "сонный" -> new SleepyState();
                default -> null;
            };

            Animal animal = new AnimalBuilder()
                    .setName(name)
                    .setBase(internalType)
                    .setHabitat(habitat)
                    .setFeeding(feeding)
                    .setTag(tag)
                    .build();

            animal.setState(state);
            environment.addObserver(animal);
            registry.register(animal);
            group.add(animal);
        }

        System.out.println("\n Жизненный цикл всех животных:");
        group.interact();

        System.out.print("\n Введите условие среды (например, пожар, ночь, шторм): ");
        String condition = scanner.nextLine().trim();
        environment.setCondition(condition);

        System.out.println("\n Выполнение команд:");
        for (Animal a : registry.getAll()) {
            new FeedCommand(a).execute();
            new MoveCommand(a, "новую локацию").execute();
        }

        scanner.close();
    }
}
