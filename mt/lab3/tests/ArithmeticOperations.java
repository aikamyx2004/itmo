public class ArithmeticOperations {
    public int sum(int a, int b) {
        return a + b;
    }

    public double g2(int a, double b) {
        return a * b / 3 % a - b * (a & a);
    }

    public static void main(String[] args) {
        int a = 1;
        ++a;
        a++;
        --a;
        a--;
        a -= 1;
        a += 1;
        a %= 2;
        double q = a + a + a - a - a - a * a * a * a / a / a / a;

    }
}
