package ru.ainur;

import ru.ainur.generator.Generator;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    private static final Path GRAMMAR_PATH = Path.of("grammar/test");
    private static final Path TEST_GRAMMAR_PATH = Path.of("src/main/java/ru/ainur/test");

    public static void main(String[] args) throws IOException {
        Generator generator = new Generator(TEST_GRAMMAR_PATH);
        generator.generate(GRAMMAR_PATH);
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
