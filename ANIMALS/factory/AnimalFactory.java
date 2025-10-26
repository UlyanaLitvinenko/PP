package factory;

import animals.*;

public class AnimalFactory {
    public static Animal create(String type) {
        return switch (type) {
            case "Dog" -> new Dog();
            case "Tiger" -> new Tiger();
            case "Dolphin" -> new Dolphin();
            default -> throw new IllegalArgumentException("Неизвестный тип: " + type);
        };
    }
}
