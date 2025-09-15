package org.example;

import java.util.Random;
import java.util.Scanner;

public class App {

    public static int[][] filingMatrix(int rows, int columns) {
        int[][] matrix = new int[rows][columns];
        Random rand = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = rand.nextInt(10);
            }
        }

        System.out.println("Полученная случайным образом матрица:");
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }

        return matrix;
    }

    public static void findTheBiggestSubsequence(int[][] matrix, int rows, int columns) {
        int count = 1, maxCount = 0, rowNumber = -1;

        for (int i = 0; i < rows; i++) {
            count = 1;
            for (int j = 1; j < columns; j++) {
                if (matrix[i][j] == matrix[i][j - 1]) {
                    count++;
                } else {
                    count = 1;
                }
                if (count > maxCount) {
                    maxCount = count;
                    rowNumber = i + 1;
                }
            }
        }

        if (maxCount <= 1) {
            System.out.println("В матрице нет серий одинаковых элементов длиной более 1.");
        } else {
            System.out.println("Строка с самой длинной серией одинаковых элементов: " + rowNumber);
            System.out.println("Длина серии: " + maxCount);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите количество строк: ");
        int rows = scanner.nextInt();
        System.out.print("Введите количество столбцов: ");
        int columns = scanner.nextInt();

        int[][] matrix = filingMatrix(rows, columns);
        findTheBiggestSubsequence(matrix, rows, columns);
    }
}
