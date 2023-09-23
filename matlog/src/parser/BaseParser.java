package parser;

import java.util.function.Predicate;


public class BaseParser {
    private static final char END = '\0';
    public final StringSource source;
    private char ch = 0xffff;

    protected BaseParser(final StringSource source) {
        this.source = source;
        take();
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean test(Predicate<Character> predicate) {
        return predicate.test(ch);
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean take(Predicate<Character> predicate) {
        if (test(predicate)) {
            take();
            return true;
        }
        return false;
    }

    private void back(int step) {
        source.back(step);
        if (step != 0) {
            take();
        }
    }

    protected boolean take(String expected) {
        for (int i = 0; i < expected.length(); i++) {
            if (!take(expected.charAt(i))) {
                back(i);
                return false;
            }
        }
        return true;
    }

    protected boolean take(final char expected) {
        return take(c -> c == expected);
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return !source.hasNext();
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
