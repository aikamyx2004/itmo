import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class WordStatWords {
    public static boolean isBadChar(char c) {
        if (Character.getType(c) == Character.DASH_PUNCTUATION || c == '\'' || Character.isLetter(c)) {
            return false;
        }
        return true;
    }

    public static void read(BufferedReader in, Map<String, Integer> wordsCount) throws IOException {
        while (true) {
            String line = in.readLine();
            if (line == null) {
                break;
            }
            line = line.toLowerCase();
            for (int begin = 0, end = 0; begin < line.length(); ) {
                end = begin;
                if (isBadChar(line.charAt(begin))) {
                    ++begin;
                } else {
                    while (end < line.length() && !isBadChar(line.charAt(end))) {
                        ++end;
                    }
                    String word = line.substring(begin, end);
                    int count = wordsCount.getOrDefault(word, 0);
                    wordsCount.put(word, count + 1);
                    begin = end;
                }
            }
        }
    }

    public static void printAnswer(BufferedWriter out, Map<String, Integer> wordsCount) throws IOException {
        for (Map.Entry<String, Integer> entry : wordsCount.entrySet()) {
            out.write(entry.getKey() + " " + entry.getValue() + "\n");
        }
    }

    public static void main(String[] args) {
        Map<String, Integer> wordsCount = new TreeMap<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "utf-8"))) {
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

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8"))) {
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
