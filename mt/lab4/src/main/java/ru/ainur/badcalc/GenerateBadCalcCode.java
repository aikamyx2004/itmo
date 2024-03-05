package ru.ainur.badcalc;

import ru.ainur.generator.Generator;

import java.io.IOException;
import java.nio.file.Path;

public class GenerateBadCalcCode {
    private static final Path BAD_CALC_GRAMMAR = Path.of("grammar/badCalc");
    private static final Path BAD_CALC_PARSER_SRC = Path.of("src/main/java/ru/ainur/badcalc");

    public static void main(String[] args) throws IOException {
        Generator generator = new Generator(BAD_CALC_PARSER_SRC);
        generator.generate(BAD_CALC_GRAMMAR);
    }
}
