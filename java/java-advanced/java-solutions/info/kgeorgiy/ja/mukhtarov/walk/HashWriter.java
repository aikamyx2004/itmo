package info.kgeorgiy.ja.mukhtarov.walk;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class HashWriter {
    private static final String ZEROS = "0".repeat(64);
    private final Writer writer;
    private final Hasher hasher = new Hasher();

    public HashWriter(Writer writer) throws NoSuchAlgorithmException {
        this.writer = writer;
    }

    private void writeAll(String hex, String path) throws IOException {
        writer.write(hex);
        writer.write(' ');
        writer.write(path);
        writer.write(System.lineSeparator());
    }

    public void write(Path path) throws IOException {
        try {
            writeAll(hasher.getHexSum(path), path.toString());
        } catch (HashCalculationException e) {
            writeZeros(path);
        }
    }

    public void writeZeros(String path) throws IOException {
        writeAll(ZEROS, path);
    }

    public void writeZeros(Path path) throws IOException {
        writeZeros(path.toString());
    }
}
