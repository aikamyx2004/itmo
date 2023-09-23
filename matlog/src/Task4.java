import expression.*;
import parser.ExpressionParser;
import util.FastScanner;
import util.Proover;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Task4 {
    public void solve() throws IOException {
        try (
//                BufferedReader reader = new BufferedReader(new FileReader("input.txt"))
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))
        ) {
            FastScanner in = new FastScanner(reader);
//            out = Files.newBufferedWriter(Path.of("output.txt"));
            out = new BufferedWriter(new PrintWriter(System.out));
            String line = in.nextLine();

            expr = PARSER.parse(line);
            if (!tryRefute()) {
                proove();
            }
            out.close();
        }
    }

    private boolean tryRefute() throws IOException {
        expr.fillVariables(variables);
        for (Map.Entry<String, Boolean> e : variables.entrySet()) {
            variableNames.add(e.getKey());
        }
        n = variables.size();
        for (int mask = 0; mask < (1 << n); mask++) {
            int i = 0;
            for (Map.Entry<String, Boolean> e : variables.entrySet()) {
                e.setValue(((mask >> i) & 1) > 0);
                ++i;
            }
            if (!expr.evaluate(variables)) {
                write("Formula is refutable ");
                write("[");
                write(variables.entrySet().stream()
                        .map(e -> String.format("%s:=%s", e.getKey(), e.getValue() ? 'T' : 'F'))
                        .collect(Collectors.joining(",")));
                write("]");
                return true;
            }
        }
        return false;
    }

    private void proove() throws IOException {
        Proover proover = new Proover(n, expr, out, variableNames);
        proover.proofAll();
    }

    private void write(String s, Object... args) throws IOException {
        out.write(String.format(s, args));
    }

    private static final ExpressionParser PARSER = new ExpressionParser();

    private final Map<String, Boolean> variables = new HashMap<>(10);
    private final ArrayList<String> variableNames = new ArrayList<>();
    BufferedWriter out;
    Expression expr;
    int n;
}
