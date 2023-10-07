import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Reverse {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new InputStreamReader(System.in
        ), new IsNumber(), System.lineSeparator());

        int[][] array = new int[1][];
        int line = 0;

        while (in.hasNext()) {
            if (line == array.length) {
                array = Arrays.copyOf(array, 2 * array.length);
            }
            array[line] = new int[1];
            int row = 0;
            while (in.hasNextInLine()) {

                if (row == array[line].length) {
                    array[line] = Arrays.copyOf(array[line], 2 * array[line].length);
                }

                array[line][row++] = in.nextInt();

            }
            array[line] = Arrays.copyOf(array[line], row);
            ++line;
        }
        array = Arrays.copyOf(array, line);

        for (int i = array.length - 1; i >= 0; i--) {
            for (int j = array[i].length - 1; j >= 0; --j) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

}
