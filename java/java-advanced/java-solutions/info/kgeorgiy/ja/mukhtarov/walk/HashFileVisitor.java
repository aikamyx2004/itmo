package info.kgeorgiy.ja.mukhtarov.walk;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class HashFileVisitor extends SimpleFileVisitor<Path> {
    private final HashWriter hashWriter;
    private final WalkType type;

    public HashFileVisitor(HashWriter writer, WalkType type) {
        this.hashWriter = writer;
        this.type = type;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        hashWriter.write(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return writeZeros(file);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (WalkType.USUAL.equals(type)) {
            return writeZeros(dir);
        }
        return FileVisitResult.CONTINUE;
    }

    private FileVisitResult writeZeros(Path path) throws IOException {
        hashWriter.writeZeros(path);
        return FileVisitResult.SKIP_SUBTREE;
    }
}
