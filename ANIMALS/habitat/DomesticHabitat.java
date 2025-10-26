package habitat;

public class DomesticHabitat implements HabitatStrategy {
    @Override
    public void live() {
        System.out.println("Живёт рядом с человеком.");
    }
}
