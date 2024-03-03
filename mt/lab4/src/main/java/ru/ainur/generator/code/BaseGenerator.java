package ru.ainur.generator.code;

import ru.ainur.generator.info.GrammarInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BaseGenerator implements AutoCloseable {
    private final Path path;
    public final GrammarInfo info;
    public final BufferedWriter writer;

    public BaseGenerator(Path path, GrammarInfo info) {
        this.path = path;
        try {
            this.writer = Files.newBufferedWriter(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not create writer", e);
        }

        this.info = info;
    }

    protected void writePackage() throws IOException {
        if (info.getPackageName() != null) {
            writer.write("package %s;%n%n".formatted(info.getPackageName()));
            writer.newLine();
        }
    }

    public void generate() {
        try {
            generateImpl();
        } catch (IOException e) {
            String message = "%s generation failed with exception %s".formatted(path.getFileName(), e.getMessage());
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }

    protected abstract void generateImpl() throws IOException;
}
