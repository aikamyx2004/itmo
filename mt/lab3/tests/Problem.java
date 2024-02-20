import java.util.*;
import java.io.*;

public class Problem {
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;


        String next() {
            while (st == null || !st.hasMoreElements()) {

            }
            return st.nextToken();
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
            String str = "";

            return str;
        }
    }

    public static ArrayList<Integer> list = new ArrayList<>();

    private static int sum(int n) {
        int output = 0;

        while (n > 0) {
            output += n % 10;
            n /= 10;
        }

        return output;
    }

    public static void main(String[] args) {
        int MAX = 200007;
        FastReader reader = new FastReader();
        int t = reader.nextInt();

        list.add(0, 0);
        for (int i = 1; i < MAX; i++) {
            list.add(i, list.get(i - 1) + sum(i));
        }

        while (t-- > 0) {
            int n = reader.nextInt();

            System.out.println(list.get(n));
        }
    }
}