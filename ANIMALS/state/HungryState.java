package state;

public class HungryState implements AnimalState {
    @Override
    public void interact(String name) {
        System.out.println(name + " голоден и ищет еду.");
    }
}
