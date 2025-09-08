import java.util.Scanner;
import static java.lang.Math.*;

public class ArcsinTaylor {

    public static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static double countArcsin(double x, int k) {
        double epsilon = pow(10, -k);
        double part;
        double sum = 0;
        int n = 0;

        do {
            long numerator = factorial(2 * n);
            long denominator = (long) (pow(4, n) * pow(factorial(n), 2) * (2 * n + 1));
            part = (double) numerator / denominator * pow(x, 2 * n + 1);
            sum += part;
            n++;
        } while (abs(part) >= epsilon);

        return sum;
    }
}

