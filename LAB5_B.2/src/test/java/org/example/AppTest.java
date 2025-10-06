package org.example;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testProcessRemovesDuplicatesAndSorts() {
        List<App.Item> input = Arrays.asList(
                new App.Item("Lamp", 102),
                new App.Item("Chair", 56),
                new App.Item("Table", 78),
                new App.Item("Lamp", 99),
                new App.Item("Chair", 120),
                new App.Item("Sofa", 88)
        );

        List<App.Item> result = App.process(input);

        assertEquals(4, result.size());

        assertEquals("Chair", result.get(0).name);
        assertEquals(56, result.get(0).code);

        assertEquals("Table", result.get(1).name);
        assertEquals(78, result.get(1).code);

        assertEquals("Sofa", result.get(2).name);
        assertEquals(88, result.get(2).code);

        assertEquals("Lamp", result.get(3).name);
        assertEquals(99, result.get(3).code);
    }
}
