package ru.ainur.generator;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import ru.ainur.generator.code.*;
import ru.ainur.generator.exception.IllegalGrammarException;
import ru.ainur.generator.info.GrammarInfo;
import ru.ainur.grammar.GrammarLexer;
import ru.ainur.grammar.GrammarParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiFunction;

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

        if (!info.isLL1()) {
            throw new IllegalGrammarException("grammar %s is not illegal".formatted(info.getGrammarName()));
        }
        generateCode(info);
    }

    private void generateCode(GrammarInfo info) {
        generateClassCode(TokenGenerator::new, info);
        generateClassCode(TreeClassesGenerator::new, info);
        generateClassCode(LexerGenerator::new, info);
        generateClassCode(ParserGenerator::new, info);
    }

    private void generateClassCode(BiFunction<Path, GrammarInfo, BaseGenerator> generator, GrammarInfo info) {
        try (var g = generator.apply(writePath, info)) {
            g.generate();
        } catch (Exception e) {
            throw new RuntimeException("could not create generator", e);
        }
    }

    private static void writeFirstFollow(GrammarInfo info) {
        System.out.println("First");
        for (var q : info.getFirst().entrySet()) {
            System.out.printf("%s: %s%n", q.getKey(), String.join(",", q.getValue()));
        }

        System.out.println("\nFollow");
        for (var q : info.getFollow().entrySet()) {
            System.out.printf("%s: %s%n", q.getKey(), String.join(",", q.getValue()));
        }
    }
}
