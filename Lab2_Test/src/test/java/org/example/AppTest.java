package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class AppTest {

    @Test
    public void testMatrixIsPrintedAndConditionIsEvaluated() {
        // Шаг 1: Подставляем ввод — 3 строки, 3 столбца
        String simulatedInput = "3\n3\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        // Шаг 2: Перехватываем вывод в консоль
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Шаг 3: Запускаем основную программу
        App.main(null);

        // Шаг 4: Получаем весь вывод как строку
        String output = outputStream.toString();

        // Шаг 5: Проверяем, что матрица была выведена
        assertTrue("Ожидается заголовок матрицы", output.contains("Полученная случайным образом матрица"));

        // Шаг 6: Проверяем, что матрица содержит хотя бы одну цифру от 0 до 9
        boolean hasDigit = false;
        for (int i = 0; i <= 9; i++) {
            if (output.contains(String.valueOf(i))) {
                hasDigit = true;
                break;
            }
        }
        assertTrue("Матрица должна содержать случайные числа от 0 до 9", hasDigit);

        // Шаг 7: Проверяем, что программа вывела результат анализа
        boolean hasResultMessage = output.contains("Строка с самой длинной серией одинаковых элементов")
                || output.contains("В матрице нет серий одинаковых элементов");
        assertTrue("Ожидается вывод результата анализа", hasResultMessage);
    }
}
