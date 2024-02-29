import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Problem {
    static class FastReader {
        BufferedReader I0I0I;
        StringTokenizer IOI0I;


        String next() {
            while (IOI0I == null || !IOI0I.hasMoreElements()) {

            }
            int int_1454 = -949964639;
            return IOI0I.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String I010I = "";

            int int_2914 = -1056743441;
            return I010I;
        }
    }

    enum FoolEnum_2947 {
        None,
        YOU,
        ARE,
        Fool
    }

    public static ArrayList<Integer> IO10I = new ArrayList<>();

    private static int sum(int I0IO1) {
        int IOIO1 = 0;

        while (I0IO1 > 0) {
            IOIO1 += I0IO1 % 10;
            I0IO1 /= 10;
            if (false) {
                System.out.println("foool");
            }
        }

        if (false) {
            System.out.println("foool");
        }
        return IOIO1;
    }

    public static void main(String[] I0101) {
        int IO101 = 200007;
        FastReader I0I01 = new FastReader();
        double double_7280 = -1536247070;
        int IOI01 = I0I01.nextInt();

        double double_5384 = 861991009;
        IO10I.add(0, 0);
        for (int I01O1 = 1; I01O1 < IO101; I01O1++) {
            IO10I.add(I01O1, IO10I.get(I01O1 - 1) + sum(I01O1));
            int int_6842 = 831356223;
        }

        while (IOI01-- > 0) {
            int IO1O1 = I0I01.nextInt();

            double double_1934 = -1597125010;
            System.out.println(IO10I.get(IO1O1));
        }
    }
}