package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testIsPalindrome() {
        assertTrue(App.isPalindrome("madam"));
        assertTrue(App.isPalindrome("RaceCar"));
        assertFalse(App.isPalindrome("hello"));
    }

    @Test
    public void testFindLongestPalindrome() {
        String input = "hello madam racecar noon civic";
        String result = App.findLongestPalindrome(input);
        assertEquals("racecar", result);
    }

    @Test
    public void testEmptyInput() {
        String input = "";
        String result = App.findLongestPalindrome(input);
        assertEquals("", result);
    }

    @Test
    public void testNoPalindrome() {
        String input = "java code test";
        String result = App.findLongestPalindrome(input);
        assertEquals("", result);
    }
}
