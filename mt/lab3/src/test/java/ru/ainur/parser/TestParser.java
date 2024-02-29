package ru.ainur.parser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TestParser {
    private static final Path TESTS_SRC = Path.of("tests");

    @Test
    public void testParser() throws IOException {
        try (Stream<Path> paths = Files.list(TESTS_SRC)) {
            paths.forEach(this::test);
        }
    }

    private void test(Path path) {
        try {
            System.out.printf("test %s%n", path);
            JavaLexer lexer = new JavaLexer(CharStreams.fromPath(path));
            TokenStream tokenStream = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokenStream);
            var file = parser.file();
            Assertions.assertNull(file.exception);
            Assertions.assertEquals(0, parser.getNumberOfSyntaxErrors());
            System.out.printf("%s is ok%n%n", path);
        } catch (IOException ignored) {
        }
    }
}
