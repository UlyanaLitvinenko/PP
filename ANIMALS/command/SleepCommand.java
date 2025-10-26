package command;

import animals.Animal;

public class SleepCommand implements Command {
    private final Animal animal;

    public SleepCommand(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void execute() {
        System.out.println(animal.getName() + " усыпляется.");
    }
}
