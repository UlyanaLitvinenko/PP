package animals;

import habitat.HabitatStrategy;
import feeding.FeedingStrategy;

public class AnimalBuilder {
    private String name;
    private Animal base;
    private HabitatStrategy habitat;
    private FeedingStrategy feeding;
    private String tag;

    public AnimalBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public AnimalBuilder setBase(String type) {
        this.base = switch (type.toLowerCase()) {
            case "dog" -> new Dog();
            case "tiger" -> new Tiger();
            case "dolphin" -> new Dolphin();
            case "cat" -> new Cat();
            case "mustang" -> new Mustang();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
        return this;
    }

    public AnimalBuilder setHabitat(HabitatStrategy habitat) {
        this.habitat = habitat;
        return this;
    }

    public AnimalBuilder setFeeding(FeedingStrategy feeding) {
        this.feeding = feeding;
        return this;
    }

    public AnimalBuilder setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public Animal build() {
        base.name = name;
        base.setHabitat(habitat);
        base.setFeeding(feeding);
        if (tag != null) {
            return new TaggedAnimal(base, tag);
        }
        return base;
    }
}
