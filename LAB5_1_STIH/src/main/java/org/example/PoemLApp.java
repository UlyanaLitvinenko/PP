/*Занести строки стихотворения одного автора в список.
Провести сортировку по возрастанию длин строк.*/
package org.example;
import java.util.*;

class PoemLine {
    private final String text;

    public PoemLine(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getLength() {
        return text.length();
    }
}

public class PoemLApp {
    public static List<PoemLine> getSortedPoem() {
        List<PoemLine> poem = new ArrayList<>();
        poem.add(new PoemLine("Наша Таня громко плачет:"));
        poem.add(new PoemLine("Уронила в речку мячик."));
        poem.add(new PoemLine("Тише, Танечка, не плачь:"));
        poem.add(new PoemLine("Не утонет в речке мяч."));

        poem.sort(Comparator.comparingInt(PoemLine::getLength));
        return poem;
    }

    public static void main(String[] args) {
        List<PoemLine> sortedPoem = getSortedPoem();
        int count = 1;
        for (PoemLine line : sortedPoem) {
            System.out.println("Строка " + count + ": " + line.getText());
            System.out.println("Длина: " + line.getLength());
            System.out.println();
            count++;
        }
    }
}
