package animals;

import java.util.ArrayList;
import java.util.List;

public class AnimalGroup extends Animal {
    private List<Animal> members = new ArrayList<>();

    public AnimalGroup(String name) {
        super(name, null);
    }

    public void add(Animal a) {
        members.add(a);
    }

    @Override
    public void interact() {
        for (Animal a : members) {
            a.liveCycle();
        }
    }
}
