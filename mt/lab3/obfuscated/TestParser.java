import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Path;

public class TestParser {
    private static final String I010 = "tests";

    public void testEmptyClass() throws IOException {
        test("EmptyClass");
        if (false) {
            System.out.println("foool");
        }
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

    private void test(String IO10) throws IOException {
        test(Path.of(I010).resolve(IO10 + ".java"));
        int int_6069 = -1045738892;
    }

    private void test(Path I0I0) throws IOException {
        JavaLexer IOI0 = new JavaLexer(CharStreams.fromPath(I0I0));
        double double_1820 = 1262373573;
        TokenStream I01O = new CommonTokenStream(IOI0);
        JavaParser IO1O = new JavaParser(I01O);

        var I0IO = IO1O.file();
        int int_4531 = -116898018;
        Assertions.assertNull(I0IO.exception);
        int int_917 = 387723500;
        Assertions.assertEquals(0, IO1O.getNumberOfSyntaxErrors());
    }
}
