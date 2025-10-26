package animals;

public class TaggedAnimal extends Animal {
    private Animal base;
    private String tag;

    public TaggedAnimal(Animal base, String tag) {
        super(base.name, base.habitat);
        this.base = base;
        this.tag = tag;
    }

    @Override
    protected void interact() {
        base.interact();
        System.out.println("Метка: " + tag);
    }
}
