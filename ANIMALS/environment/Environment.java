package environment;

import animals.Animal;

import java.util.ArrayList;
import java.util.List;

public class Environment {
    private String condition;
    private final List<Animal> observers = new ArrayList<>();

    public void addObserver(Animal animal) {
        observers.add(animal);
    }

    public void setCondition(String condition) {
        this.condition = condition;
        notifyObservers();
    }

    public String getCondition() {
        return condition;
    }

    private void notifyObservers() {
        for (Animal a : observers) {
            a.reactToEnvironment(condition);
        }
    }
}
