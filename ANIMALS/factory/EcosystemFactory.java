package factory;

import animals.Animal;
import animals.TaggedAnimal;

public class EcosystemFactory {
    public static Animal createTagged(String type, String tag) {
        return new TaggedAnimal(AnimalFactory.create(type), tag);
    }
}
