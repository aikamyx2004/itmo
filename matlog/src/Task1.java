import expression.Expression;
import parser.ExpressionParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Task1 {
    public void solve() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            try (BufferedWriter out = new BufferedWriter(new PrintWriter(System.out))) {
                ExpressionParser parser = new ExpressionParser();
                Expression expr = parser.parse(reader.readLine());
                out.write(expr.parseTree());
            }
        }
    }
}
