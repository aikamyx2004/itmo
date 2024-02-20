import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class TestParser {
    private static final String PARSER_TESTS_SRC = "tests";

    public void testEmptyClass() throws IOException {
        test("EmptyClass");
    }

    public void testPsvm() throws IOException {
        test("Psvm");
    }

    public void testSomeFunctions() throws IOException {
        test("SomeFunctions");
    }

    public void testArithmeticOperations() throws IOException {
        test("ArithmeticOperations");
    }

    public void testVariables() throws IOException {
        test("Variables");
    }

    public void testGenerics() throws IOException {
        test("Generics");
    }

    private void test(String filename) throws IOException {
        test(Path.of(PARSER_TESTS_SRC).resolve(filename + ".java"));
    }

    private void test(Path path) throws IOException {
        JavaLexer lexer = new JavaLexer(CharStreams.fromPath(path));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);

        var file = parser.file();
        Assertions.assertNull(file.exception);
        Assertions.assertEquals(0, parser.getNumberOfSyntaxErrors());
    }
}
