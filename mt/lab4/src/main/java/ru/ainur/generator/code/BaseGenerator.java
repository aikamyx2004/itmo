package ru.ainur.generator.code;

import ru.ainur.generator.GrammarInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BaseGenerator {
    public final Path path;
    public final GrammarInfo info;

    public BaseGenerator(Path path, GrammarInfo info) {
        this.path = path;
        this.info = info;
    }

    protected void writeHeader(BufferedWriter writer) throws IOException {
        if (info.getPackageName() != null) {
            writer.write("package %s;%n%n".formatted(info.getPackageName()));
            writer.newLine();
        }
    }

    public void generate() {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            generateImpl(writer);
        } catch (IOException e) {
            String message = "%s generation failed with exception %s".formatted(path.getFileName(), e.getMessage());
            throw new RuntimeException(message, e);
        }
    }

    protected abstract void generateImpl(BufferedWriter writer) throws IOException;
}
