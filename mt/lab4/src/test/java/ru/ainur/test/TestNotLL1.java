package ru.ainur.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ainur.generator.Generator;
import ru.ainur.generator.exception.IllegalGrammarException;

import java.nio.file.Path;

public class TestNotLL1 {
    private static final Path NOT_LL1_PARSER_SRC = Path.of("test");
    private static final Path NOT_LL1_GRAMMAR = Path.of("grammar/notLL1");

    @Test
    public void notLL1() {
        Assertions.assertThrows(IllegalGrammarException.class, ()->{
            Generator generator = new Generator(NOT_LL1_PARSER_SRC);
            generator.generate(NOT_LL1_GRAMMAR);
        });
    }
}
