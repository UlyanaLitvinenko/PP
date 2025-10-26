package command;

import animals.Animal;

public class FeedCommand implements Command {
    private final Animal animal;

    public FeedCommand(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void execute() {
        System.out.println(animal.getName() + " получает еду.");
    }
}
