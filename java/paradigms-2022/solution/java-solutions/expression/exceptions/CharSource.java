package expression.exceptions;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface CharSource {
    boolean hasNext();

    char next();

    void back();

    char get();

    IllegalArgumentException error(final String message);
}
