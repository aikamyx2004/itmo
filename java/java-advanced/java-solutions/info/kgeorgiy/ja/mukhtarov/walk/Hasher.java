package info.kgeorgiy.ja.mukhtarov.walk;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Hasher {
    private static final int BUFFER_SIZE = 1 << 16;
    private final byte[] buffer = new byte[BUFFER_SIZE];
    private final MessageDigest digest;
    private final HexFormat hexFormat = HexFormat.of();

    public Hasher() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-256");
    }

    public String getHexSum(Path path) throws HashCalculationException {
        try {
            digest.reset();
            DigestInputStream input = new DigestInputStream(Files.newInputStream(path), digest);
            while (input.read(buffer) > 0) {
                //no operations
            }
            return hexFormat.formatHex(digest.digest());
        } catch (IOException e) {
            throw new HashCalculationException("Could not calculate hash");
        }
    }
}
