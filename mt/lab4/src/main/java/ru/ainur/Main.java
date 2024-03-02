package ru.ainur;

import ru.ainur.generator.Generator;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    private static final Path TEST_GRAMMAR = Path.of("grammar/test");
    private static final Path TEST_PARSER_SRC = Path.of("src/main/java/ru/ainur/test");

    private static final Path LAB1_GRAMMAR = Path.of("grammar/lab1");
    private static final Path LAB1_PARSER_SRC = Path.of("src/main/java/ru/ainur/lab1");

    public static void main(String[] args) throws IOException {
        Generator generator = new Generator(LAB1_PARSER_SRC);
        generator.generate(LAB1_GRAMMAR);
//        CharStream charStream = CharStreams.fromPath(GRAMMAR_PATH);
//        GrammarLexer lexer = new GrammarLexer(charStream);
//        TokenStream tokenStream = new CommonTokenStream(lexer);
//        GrammarParser parser = new GrammarParser(tokenStream);
//
//        InfoListener listener = new InfoListener();
//        parser.addParseListener(listener);
//
    }
}
