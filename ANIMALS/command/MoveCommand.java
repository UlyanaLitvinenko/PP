package command;

import animals.Animal;

public class MoveCommand implements Command {
    private final Animal animal;
    private final String location;

    public MoveCommand(Animal animal, String location) {
        this.animal = animal;
        this.location = location;
    }

    @Override
    public void execute() {
        System.out.println(animal.getName() + " перемещается в " + location + ".");
    }
}
