package ru.ainur.obfuscator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import ru.ainur.listener.JavaListener;
import ru.ainur.listener.ReplaceListener;
import ru.ainur.parser.JavaLexer;
import ru.ainur.parser.JavaParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Obfuscator {
    private final Path writeDir;

    public Obfuscator(Path writeDir) {
        this.writeDir = writeDir;
        try {
            Files.createDirectories(writeDir);
        } catch (IOException e) {
            // no operations
        }
    }

    public void handle(Path path) {
        Path writePath = writeDir.resolve(path.getFileName());
        try (BufferedWriter writer = Files.newBufferedWriter(writePath)) {
            writeObfuscatedFile(path, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeObfuscatedFile(Path path, BufferedWriter writer) throws IOException {
        JavaParser parser = parseJavaFile(path);
        JavaListener listener = new JavaListener();

        parser.addParseListener(listener);
        var file = parser.file();
        // remove EOF
        file.removeLastChild();

        var variables = listener.getVariables();
        var newNames = ObfuscatorUtil.getNewNames(variables);

        ParseTreeWalker walker = new ParseTreeWalker();

        ReplaceListener replaceListener = new ReplaceListener(newNames, parser);

        walker.walk(replaceListener, file);

        writer.write(replaceListener
                .getTokenStreamRewriter()
                .getText());
    }

    private JavaParser parseJavaFile(Path path) throws IOException {
        JavaLexer lexer = new JavaLexer(CharStreams.fromPath(path));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        return new JavaParser(tokenStream);
    }
}
