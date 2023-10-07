import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;


public class Wspp {
    public static void read(Scanner in, Map<String, IntList> wordsCount) throws IOException {
        int index = 1;
        while (in.hasNext()) {
            while (in.hasNextInLine()) {
                String word = in.next().toLowerCase();
                wordsCount.putIfAbsent(word, new IntList());
                wordsCount.get(word).add(index++);
            }
        }
    }

    public static void printAnswer(BufferedWriter out, Map<String, IntList> wordsCount) throws IOException {
        for (Map.Entry<String, IntList> entry : wordsCount.entrySet()) {
            String word = entry.getKey();
            IntList indexes = entry.getValue();

            out.write(word);
            out.write(' ');
            out.write(String.valueOf(indexes.size()));
            for (int i = 0; i < indexes.size(); i++) {
                out.write(" ");
                out.write(String.valueOf(indexes.get(i)));
            }
            out.write('\n');
        }
    }

    public static void main(String[] args) {
        Map<String, IntList> wordsCount = new LinkedHashMap<>();
        try (Scanner in = new Scanner(new InputStreamReader(new FileInputStream(args[0]), "utf-8"),
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
