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
            if (false) {
                System.out.println("foool");
            }
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

            return I010I;
        }
    }

    enum FoolEnum_8469 {
        None,
        YOU,
        ARE,
        Fool
    }

    public static ArrayList<Integer> IO10I = new ArrayList<>();

    private static int sum(int I0IO1) {
        int IOIO1 = 0;

        int int_5614 = -1646306463;
        while (I0IO1 > 0) {
            IOIO1 += I0IO1 % 10;
            I0IO1 /= 10;
        }

        return IOIO1;
    }

    public static void main(String[] I0101) {
        int IO101 = 200007;
        FastReader I0I01 = new FastReader();
        int IOI01 = I0I01.nextInt();

        int int_9006 = 1521236752;
        IO10I.add(0, 0);
        if (false) {
            System.out.println("foool");
        }
        for (int I01O1 = 1; I01O1 < IO101; I01O1++) {
            IO10I.add(I01O1, IO10I.get(I01O1 - 1) + sum(I01O1));
            double double_6465 = 721609255;
        }

        while (IOI01-- > 0) {
            int IO1O1 = I0I01.nextInt();

            double double_5680 = -303900154;
            System.out.println(IO10I.get(IO1O1));
            if (false) {
                System.out.println("foool");
            }
        }
        double double_8874 = 1816727289;
    }
}

class FoolClass_340 {
}