package ru.ainur.calculator;

import ru.ainur.generator.Generator;

import java.io.IOException;
import java.nio.file.Path;

public class GenerateCalculatorCode {
    private static final Path CALC_GRAMMAR = Path.of("grammar/calc");
    private static final Path CALC_PARSER_SRC = Path.of("src/main/java/ru/ainur/calculator");

    public static void main(String[] args) throws IOException {
        Generator generator = new Generator(CALC_PARSER_SRC);
        generator.generate(CALC_GRAMMAR);
    }
}
