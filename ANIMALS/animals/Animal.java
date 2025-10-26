package animals;

import habitat.HabitatStrategy;
import state.AnimalState;
import feeding.FeedingStrategy;

public abstract class Animal {
    protected String name;
    protected HabitatStrategy habitat;
    protected AnimalState state;
    protected FeedingStrategy feeding;

    public Animal(String name, HabitatStrategy habitat) {
        this.name = name;
        this.habitat = habitat;
    }

    public final void liveCycle() {
        System.out.println(name + " рождается.");
        if (habitat != null) {
            habitat.live();
        } else {
            System.out.println(name + " не знает, где живёт.");
        }
        if (feeding != null) {
            feeding.feed(name);
        }
        interact();
        System.out.println(name + " отдыхает.\n");
    }

    protected void interact() {
        if (state != null) {
            state.interact(name);
        } else {
            System.out.println(name + " взаимодействует по умолчанию.");
        }
    }

    public void setHabitat(HabitatStrategy habitat) {
        this.habitat = habitat;
    }

    public void setState(AnimalState state) {
        this.state = state;
    }

    public void setFeeding(FeedingStrategy feeding) {
        this.feeding = feeding;
    }

    public void reactToEnvironment(String condition) {
        System.out.println(name + " замечает изменение среды: " + condition);
    }

    public String getName() {
        return name;
    }
}
