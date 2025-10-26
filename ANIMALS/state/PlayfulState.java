package state;

public class PlayfulState implements AnimalState {
    @Override
    public void interact(String name) {
        System.out.println(name + " играет и веселится.");
    }
}
