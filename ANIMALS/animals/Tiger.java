package animals;

import habitat.WildHabitat;

public class Tiger extends Animal {
    public Tiger() {
        super("Тигр", new WildHabitat());
    }

    @Override
    protected void interact() {
        System.out.println("Тигр рычит и охотится.");
    }
}
