package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testLatin() {
        assertEquals("abcdefghi", App.decode("adgbehcfi"));
    }



    @Test
    public void testShort() {
        assertEquals("abc", App.decode("abc"));
    }

    @Test
    public void testSingleChar() {
        assertEquals("a", App.decode("a"));
    }

    @Test
    public void testEvenLength() {
        assertEquals("abcdef", App.decode("adbecf"));
    }
}
