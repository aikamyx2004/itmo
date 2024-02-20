import java.util.ArrayList;
import java.util.List;

public class SomeFunctions {

    public static int f1(int a, int v) {
        return a * v;
    }

    private static int f2(int a, int c) {
        return f1(a, c) * f1(c, a * c);
    }

    public static void main(String[] args) {
        int a = 8;
        a += 1;
        a += a;
        a /= a;
        List<Integer> b = new ArrayList<>();
        f1(112, 2);
        f1(f2(12, 4), 12);
    }
}
