package org.example;

import java.util.*;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder fullText = new StringBuilder();

        System.out.println("Введите текст построчно. Пустая строка завершает ввод:");

        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) break;
            fullText.append(line).append(" ");
        }

        String longestPalindrome = findLongestPalindrome(fullText.toString());
        System.out.println("Самый длинный палиндром: " + longestPalindrome);
    }

    public static String findLongestPalindrome(String text) {
        String[] words = text.split("[\\s,\\.]+");
        String longest = "";

        for (String word : words) {
            if (isPalindrome(word) && word.length() > longest.length()) {
                longest = word;
            }
        }

        return longest;
    }

    public static boolean isPalindrome(String word) {
        String lower = word.toLowerCase();
        int left = 0, right = lower.length() - 1;

        while (left < right) {
            if (lower.charAt(left) != lower.charAt(right)) return false;
            left++;
            right--;
        }

        return true;
    }
}
