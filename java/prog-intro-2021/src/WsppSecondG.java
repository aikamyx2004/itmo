import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;


public class WsppSecondG {
    private static void read(Scanner in, Map<String, Count> wordsCount) throws IOException {
        int index = 1;
        while (in.hasNext()) {
            Map<String, IntList> countInLine = new LinkedHashMap<>();
            while (in.hasNextInLine()) {
                String word = in.next().toLowerCase();
                countInLine.putIfAbsent(word, new IntList());
                countInLine.get(word).add(index++);
            }

            for (Map.Entry<String, IntList> entry : countInLine.entrySet()) {
                String word = entry.getKey();
                IntList list = entry.getValue();
                wordsCount.putIfAbsent(word, new Count());
                wordsCount.get(word).addCount(list.size());
                for (int i = 1; i < list.size(); i += 2) {
                    wordsCount.get(word).add(list.get(i));
                }
            }
        }
    }

    private static void printAnswer(BufferedWriter out, Map<String, Count> wordsCount) throws IOException {
        for (Map.Entry<String, Count> entry : wordsCount.entrySet()) {
            String word = entry.getKey();
            IntList indexes = entry.getValue().getList();

            out.write(word);
            out.write(' ');
            out.write(String.valueOf(wordsCount.get(word).getCount()));
            for (int i = 0; i < indexes.size(); i++) {
                out.write(" ");
                out.write(String.valueOf(indexes.get(i)));
            }
            out.write('\n');
        }
    }

    public static void main(String[] args) {
        Map<String, Count> wordsCount = new LinkedHashMap<>();

        try (Scanner in = new Scanner(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8),
                new IsLetter(), System.lineSeparator())) {
            try {
                read(in, wordsCount);
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file: " + args[0] + "\n" + e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
            try {
                printAnswer(out, wordsCount);
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file: " + args[1] + "\n" + e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
