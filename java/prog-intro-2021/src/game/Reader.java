package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class Reader {
    protected static <T> List<T> readTokens(int count, Scanner in, Function<String, T> function) {
//        boolean f = true;
        while (true) {
            try {
                List<T> result = new ArrayList<>();
                String line = null;
                do {
                    line = in.nextLine();
                } while (line.isEmpty());
                try (Scanner lineScanner = new Scanner(line)) {
                    for (int i = 0; i < count; i++) {
                        String item = lineScanner.next().toLowerCase();
                        result.add(function.apply(item));
                    }
//                    f = true;
                    if (lineScanner.hasNext()) {
                        System.out.println("You wrote too many data, please write again");
                        continue;
                    }
                    return result;
                }

            } catch (Exception e) {
//                if (f) {
                System.out.println("Wrong format, please write again");
//                    f = false;
//                }
            }
        }
    }

    protected static List<Integer> readIntegers(int count, Scanner in, int from) {
        outer:
        while (true) {
            List<Integer> result = Reader.readTokens(count, in, Integer::parseInt);
            for (int number : result) {
                if (number < from) {
                    System.out.printf("Wrong format, %d < 0\n", from);
                    continue outer;
                }
            }
            return result;
        }
    }
}

