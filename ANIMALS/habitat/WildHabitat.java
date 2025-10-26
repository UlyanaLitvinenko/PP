package habitat;

public class WildHabitat implements HabitatStrategy {
    @Override
    public void live() {
        System.out.println("Обитает в дикой природе.");
    }
}
