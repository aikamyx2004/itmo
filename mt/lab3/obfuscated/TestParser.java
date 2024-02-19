package ru.ainur.parser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class TestParser {
    private static final String I0101 = "tests";

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

    private void test(String IO101) throws IOException {
        test(Path.of(I0101).resolve(IO101 + ".java"));
    }

    private void test(Path I0I01) throws IOException {
        JavaLexer IOI01 = new JavaLexer(CharStreams.fromPath(I0I01));
        TokenStream I01O1 = new CommonTokenStream(IOI01);
        JavaParser IO1O1 = new JavaParser(I01O1);

        var I0IO1 = IO1O1.file();
        Assertions.assertNull(I0IO1.exception);
        Assertions.assertEquals(0, IO1O1.getNumberOfSyntaxErrors());
    }
}
