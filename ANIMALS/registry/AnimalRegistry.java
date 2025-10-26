package registry;

import animals.Animal;

import java.util.ArrayList;
import java.util.List;

public class AnimalRegistry {
    private static AnimalRegistry instance;
    private final List<Animal> animals = new ArrayList<>();

    private AnimalRegistry() {}

    public static AnimalRegistry getInstance() {
        if (instance == null) {
            instance = new AnimalRegistry();
        }
        return instance;
    }

    public void register(Animal animal) {
        animals.add(animal);
        System.out.println("Зарегистрировано животное: " + animal.getClass().getSimpleName());
    }

    public List<Animal> getAll() {
        return animals;
    }

    public void printAll() {
        System.out.println("📋 Все зарегистрированные животные:");
        for (Animal a : animals) {
            System.out.println("- " + a.getClass().getSimpleName());
        }
    }
}
