import java.util.ArrayList;
import java.util.List;

public class SomeFunctions {

    public static int f1(int I010, int IO10) {
        return I010 * IO10;
    }

    private static int f2(int I0I0, int IOI0) {
        return f1(I0I0, IOI0) * f1(IOI0, I0I0 * IOI0);
    }

    public static void main(String[] I01O) {
        int IO1O = 8;
        IO1O += 1;
        IO1O += IO1O;
        IO1O /= IO1O;
        int int_7439 = 853240929;
        List<Integer> I0IO = new ArrayList<>();
        int int_4611 = -806312310;
        f1(112, 2);
        f1(f2(12, 4), 12);
    }
}
