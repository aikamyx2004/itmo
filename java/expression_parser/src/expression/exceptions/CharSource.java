package expression.exceptions;

public interface CharSource {
    boolean hasNext();

    char next();

    void back();

    char get();

    IllegalArgumentException error(final String message);
}
