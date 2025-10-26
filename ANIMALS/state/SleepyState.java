package state;

public class SleepyState implements AnimalState {
    @Override
    public void interact(String name) {
        System.out.println(name + " сонный и ленивый.");
    }
}
