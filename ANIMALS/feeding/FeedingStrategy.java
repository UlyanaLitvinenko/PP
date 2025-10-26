package feeding;

public class FeedingStrategy {
    public enum Type {
        CARNIVORE, HERBIVORE, OMNIVORE
    }

    private final Type type;

    public FeedingStrategy(Type type) {
        this.type = type;
    }

    public void feed(String name) {
        switch (type) {
            case CARNIVORE -> System.out.println(name + " охотится на добычу.");
            case HERBIVORE -> System.out.println(name + " поедает траву и листья.");
            case OMNIVORE -> System.out.println(name + " питается всем подряд.");
        }
    }

    public Type getType() {
        return type;
    }
}
