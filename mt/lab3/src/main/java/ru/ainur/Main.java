package ru.ainur;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Main {
    private static final Path TESTS_SRC = Path.of("tests");
    private static final Path WRITE_SRC = Path.of("obfuscated");

    public static void main(String[] args) throws IOException {
        Obfuscator obfuscator = new Obfuscator(WRITE_SRC);
        try (Stream<Path> paths = Files.list(TESTS_SRC)) {
            paths.forEach(obfuscator::handle);
        }
    }

}
