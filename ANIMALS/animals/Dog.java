package animals;

import habitat.DomesticHabitat;

public class Dog extends Animal {
    public Dog() {
        super("Собака", new DomesticHabitat());
    }

    @Override
    protected void interact() {
        System.out.println("Собака лает и охраняет.");
    }
}
