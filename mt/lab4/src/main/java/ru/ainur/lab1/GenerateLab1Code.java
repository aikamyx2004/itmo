package ru.ainur.lab1;

import ru.ainur.generator.Generator;

import java.io.IOException;
import java.nio.file.Path;

public class GenerateLab1Code {
    private static final Path LAB1_GRAMMAR = Path.of("grammar/lab1");
    private static final Path LAB1_PARSER_SRC = Path.of("src/main/java/ru/ainur/lab1");

    public static void main(String[] args) throws IOException {
        Generator generator = new Generator(LAB1_PARSER_SRC);
        generator.generate(LAB1_GRAMMAR);
    }
}
