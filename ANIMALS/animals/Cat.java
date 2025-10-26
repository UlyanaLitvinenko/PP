package animals;

public class Cat extends Animal {
    public Cat() {
        super("Кот", null);
    }

    @Override
    protected void interact() {
        System.out.println(name + " мурлычет и охотится.");
    }
}
