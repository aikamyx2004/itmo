package ru.ainur.generator;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import ru.ainur.generator.code.LexerGenerator;
import ru.ainur.generator.code.ParserGenerator;
import ru.ainur.generator.code.TokenGenerator;
import ru.ainur.generator.code.TreeClassesGenerator;
import ru.ainur.grammar.GrammarLexer;
import ru.ainur.grammar.GrammarParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Generator {
    private final Path writePath;


    public Generator(Path writePath) {
        this.writePath = writePath;
        try {
            Files.createDirectories(writePath);
        } catch (IOException ignored) {
        }
    }

    public void generate(Path grammarPath) throws IOException {
        CharStream charStream = CharStreams.fromPath(grammarPath);
        GrammarLexer lexer = new GrammarLexer(charStream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokenStream);
        InfoListener infoListener = new InfoListener();
        parser.addParseListener(infoListener);
        var grammar = parser.startRule();
        GrammarInfo info = infoListener.getInfo();
        info.countFirstFollow();
        System.out.println("First");
        for (var q : info.getFirst().entrySet()) {
            System.out.printf("%s: %s%n", q.getKey(), String.join(",", q.getValue()));
        }

        System.out.println("\nFollow");
        for (var q : info.getFollow().entrySet()) {
            System.out.printf("%s: %s%n", q.getKey(), String.join(",", q.getValue()));
        }
        new TokenGenerator(writePath, info).generate();
        new TreeClassesGenerator(writePath, info).generate();
        new LexerGenerator(writePath, info).generate();
        new ParserGenerator(writePath, info).generate();

    }
}
