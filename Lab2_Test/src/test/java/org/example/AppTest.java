package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class AppTest {

    @Test
    public void testMatrixIsPrintedAndConditionIsEvaluated() {
        String simulatedInput = "3\n3\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        App.main(null);

        String output = outputStream.toString();

        assertTrue("Ожидается заголовок матрицы", output.contains("Полученная случайным образом матрица"));

        boolean hasDigit = false;
        for (int i = 0; i <= 9; i++) {
            if (output.contains(String.valueOf(i))) {
                hasDigit = true;
                break;
            }
        }
        assertTrue("Матрица должна содержать случайные числа от 0 до 9", hasDigit);

        boolean hasResultMessage = output.contains("Строка с самой длинной серией одинаковых элементов")
                || output.contains("В матрице нет серий одинаковых элементов");
        assertTrue("Ожидается вывод результата анализа", hasResultMessage);
    }
}
