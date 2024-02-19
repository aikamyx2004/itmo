package ru.ainur;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import ru.ainur.parser.JavaLexer;
import ru.ainur.parser.JavaParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Obfuscator {
    private final Path writeDir;

    public Obfuscator(Path writeDir) {
        this.writeDir = writeDir;
    }

    public void handle(Path path) {
        Path writePath = writeDir.resolve(path.getFileName());
        try (BufferedWriter writer = Files.newBufferedWriter(writePath)) {
            JavaLexer lexer = new JavaLexer(CharStreams.fromPath(path));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokenStream);
            JavaListener listener = new JavaListener();

            parser.addParseListener(listener);
            var file = parser.file();
            file.removeLastChild();
            writeObfuscatedFile(file, listener, tokenStream, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeObfuscatedFile(JavaParser.FileContext file, JavaListener listener, CommonTokenStream tokenStream, BufferedWriter writer) throws IOException {

        var variables = listener.getVariables();


        int countNames = getSummedLengths(variables);

        Map<List<String>, Map<String, String>> newNames = getNewNames(countNames, variables);
        ParseTreeWalker walker = new ParseTreeWalker();
        WriteListener visitor = new WriteListener(newNames, tokenStream);

        walker.walk(visitor, file);

        writer.write(file.getText());

    }

    private Map<List<String>, Map<String, String>> getNewNames(int count, Map<List<String>, List<String>> variables) {
        int current = 0;

        List<String> names = generateNewNames(count);
        Map<List<String>, Map<String, String>> newNames = new HashMap<>();
        for (var e : variables.entrySet()) {
            newNames.putIfAbsent(e.getKey(), new HashMap<>());
            for (String curName : e.getValue()) {
                newNames.get(e.getKey()).put(curName, names.get(current));
                current++;
            }
        }
        return newNames;
    }

    private List<String> generateNewNames(int count) {
        int base = getBase(count) + 2;
        List<String> names = new ArrayList<>();
        for (int i = 0; names.size() < count; i++) {
            String name = generateName(base, i);
            if (correctName(name)) {
                names.add(name);
            }
        }
        return names;
    }

    private boolean correctName(String name) {
        return !Character.isDigit(name.charAt(0));
    }

    private static int getSummedLengths(Map<List<String>, List<String>> variables) {
        return variables.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    private static int getBase(int n) {
        int c = 0;
        while (n > 0) {
            n /= 2;
            c++;
        }
        return c;
    }

    private static String generateName(int base, int i) {
        StringBuilder result = new StringBuilder();
        char[][] symbols = new char[][]{{'1', 'I'}, {'0', 'O'}};
        for (int j = 0; j < base; j++) {
            result.append(symbols[j % 2][(i >> j) & 1]);
        }

        return result.toString();
    }
}
