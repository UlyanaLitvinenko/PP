package habitat;

public class AquaticHabitat implements HabitatStrategy {
    @Override
    public void live() {
        System.out.println("Обитает в воде.");
    }
}
