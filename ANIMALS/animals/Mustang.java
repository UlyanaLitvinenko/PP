package animals;

public class Mustang extends Animal {
    public Mustang() {
        super("Мустанг", null);
    }

    @Override
    protected void interact() {
        System.out.println(name + " скачет по равнине.");
    }
}
