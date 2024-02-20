import java.util.*;
import java.io.*;

public class Codechef {
    static class FastReader {
        BufferedReader I0I01;
        StringTokenizer IOI01;



        String next() {
            while (IOI01 == null || !IOI01.hasMoreElements()) {

            }
            return IOI01.nextToken();
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
            String IO1O1 = "";

            return IO1O1;
        }
    }

    public static ArrayList<Integer> I01O1 = new ArrayList<>();

    private static int sum(int I0101) {
        int IO101 = 0;

        while (I0101 > 0) {
            IO101 += I0101 % 10;
            I0101 /= 10;
        }

        return IO101;
    }

    public static void main(String[] I0IO1) {
        int IOIO1 = 200007;
        FastReader I010I = new FastReader();
        int IO10I = I010I.nextInt();

        I01O1.add(0, 0);
        for (int I0I0I = 1; I0I0I < IOIO1; I0I0I++) {
            I01O1.add(I0I0I, I01O1.get(I0I0I - 1) + sum(I0I0I));
        }

        while (IO10I-- > 0) {
            int IOI0I = I010I.nextInt();

            System.out.println(I01O1.get(IOI0I));
        }
    }
}