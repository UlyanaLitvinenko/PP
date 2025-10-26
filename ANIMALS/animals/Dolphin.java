package animals;

import habitat.AquaticHabitat;

public class Dolphin extends Animal {
    public Dolphin() {
        super("Дельфин", new AquaticHabitat());
    }

    @Override
    protected void interact() {
        System.out.println("Дельфин плавает и играет.");
    }
}
