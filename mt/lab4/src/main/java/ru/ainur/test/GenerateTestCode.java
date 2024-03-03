package ru.ainur.test;

import ru.ainur.generator.Generator;

import java.io.IOException;
import java.nio.file.Path;

public class GenerateTestCode {
    private static final Path TEST_GRAMMAR = Path.of("grammar/test");
    private static final Path TEST_PARSER_SRC = Path.of("src/main/java/ru/ainur/test");

    public static void main(String[] args) throws IOException {
        Generator generator = new Generator(TEST_PARSER_SRC);
        generator.generate(TEST_GRAMMAR);
    }
}
