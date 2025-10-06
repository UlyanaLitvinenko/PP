package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PoemAppTest {

    @Test
    void testSortedPoemByLength() {
        List<PoemLine> sorted = PoemLApp.getSortedPoem();

        for (int i = 0; i < sorted.size() - 1; i++) {
            int currentLength = sorted.get(i).getLength();
            int nextLength = sorted.get(i + 1).getLength();
            assertTrue(currentLength <= nextLength, "Сортировка нарушена между строками " + i + " и " + (i + 1));
        }
    }

    @Test
    void testPoemIntegrity() {
        List<PoemLine> sorted = PoemLApp.getSortedPoem();
        assertEquals(4, sorted.size(), "Количество строк должно быть 4");
        assertTrue(sorted.stream().anyMatch(line -> line.getText().contains("Таня")), "Строка с Таней должна присутствовать");
    }
}
