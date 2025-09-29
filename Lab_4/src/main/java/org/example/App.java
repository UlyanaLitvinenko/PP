package org.example;

public class App {
    public static String decode(String s) {
        int n = s.length();
        int len1 = (n + 2) / 3;
        int len2 = (n + 1) / 3;
        int len3 = n / 3;

        String a = "", b = "", c = "";
        for (int i = 0; i < len1; i++) a += s.charAt(i);
        for (int i = len1; i < len1 + len2; i++) b += s.charAt(i);
        for (int i = len1 + len2; i < n; i++) c += s.charAt(i);

        String res = "";
        int i1 = 0, i2 = 0, i3 = 0;
        for (int i = 0; i < n; i++) {
            if (i % 3 == 0) res += a.charAt(i1++);
            else if (i % 3 == 1) res += b.charAt(i2++);
            else res += c.charAt(i3++);
        }

        return res;
    }

    public static void main(String[] args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        String input = sc.nextLine();
        System.out.println(decode(input));
    }
}
