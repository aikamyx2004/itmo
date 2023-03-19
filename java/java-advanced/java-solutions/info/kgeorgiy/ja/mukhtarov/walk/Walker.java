package info.kgeorgiy.ja.mukhtarov.walk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class Walker {
    public void walk(String[] args, WalkType type) {
        if (!correctArgs(args)) {
            return;
        }

        Path inputFile = getPath(args[0], "Wrong first argument:");
        Path outputFile = getPath(args[1], "Wrong second argument:");
        if (inputFile == null || outputFile == null) {
            return;
        }

        try {
            if (outputFile.getParent() != null) {
                Files.createDirectories(outputFile.getParent());
            }
        } catch (IOException e) {
            // no operations
        }
        try (BufferedReader reader = Files.newBufferedReader(inputFile)) {
            try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
                HashWriter hashWriter = new HashWriter(writer);
                HashFileVisitor visitor = new HashFileVisitor(hashWriter, type);

                while (true) {
                    String pathString;
                    try {
                        if ((pathString = reader.readLine()) == null) {
                            break;
                        }
                    } catch (IOException e) {
                        error("Could not read from input " + e);
                        break;
                    }
                    try {
                        Files.walkFileTree(Path.of(pathString), visitor);
                    } catch (InvalidPathException e) {
                        hashWriter.writeZeros(pathString);
                    } catch (IOException e) {
                        error("Could not write to output " + e);
                        break;
                    }
                }
            } catch (IOException e) {
                error("Could not create writer " + e);
            }
        } catch (IOException e) {
            error("Could not create reader " + e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("There is no SHA-256 algorithm", e);
        }
    }

    private void error(String message) {
        System.err.println(message);
    }

    private boolean correctArgs(String[] args) {
        if (args == null || args.length != 2) {
            error("Wrong arguments, should be exactly 2 arguments. Usage: <input file> <output file>");
            return false;
        }
        return true;
    }


    private Path getPath(String path, String message) {
        if (path == null) {
            error(message + " path is null.");
            return null;
        }
        try {
            return Path.of(path);
        } catch (InvalidPathException e) {
            error(message + " Invalid path: " + e);
            return null;
        }
    }
}
