import java.io.IOException;
import java.io.Reader;

public class Scanner implements AutoCloseable {
    private Reader in;
    private char[] buffer;
    private int pos, end;
    private final String separator;
    private final CheckChar checker;

    public Scanner(Reader in, CheckChar checker, String separator) {
        this.in = in;
        this.buffer = new char[1 << 12];
        this.pos = this.end = 0;
        this.separator = separator;
        this.checker = checker;
    }

    @Override
    public void close() throws IOException {
        if (in == null) {
            return;
        }
        try {
            in.close();
        } finally {
            in = null;
            buffer = null;
        }
    }

    private void checkIsClosed() throws IOException {
        if (in == null) {
            throw new IOException("Scanner closed");
        }
    }

    private void fillBuffer() throws IOException {
        if (pos == end) {
            end = in.read(buffer, 0, buffer.length);
            pos = 0;
        }
    }


    boolean endsWithSep(StringBuilder a) {
        if (separator == null || a.length() < separator.length()) return false;
        for (int i = 0; i < separator.length(); i++) {
            if (a.charAt(a.length() - separator.length() + i) != separator.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    boolean hasNext() throws IOException {
//        checkIsClosed();
        while (true) {
            fillBuffer();
            if (pos >= end || isInSeparator(buffer[pos]) || checker.isGoodChar(buffer[pos])) {
                break;
            }
            ++pos;
        }
        return pos < end && (isInSeparator(buffer[pos]) || checker.isGoodChar(buffer[pos]));
    }

    boolean isInSeparator(char c) {
        return separator != null && separator.indexOf(c) != -1;
    }

    boolean hasNextInLine() throws IOException {
//        checkIsClosed();
        if (pos >= end) {
            return false;
        }
        while (true) {
            fillBuffer();
            if (pos >= end || isInSeparator(buffer[pos]) || checker.isGoodChar(buffer[pos])) {
                break;
            }
            ++pos;
        }
        StringBuilder sep = new StringBuilder();
        while (true) {
            fillBuffer();
            if (pos >= end || sep.length() == separator.length() || !isInSeparator(buffer[pos])) {
                break;
            }
            sep.append(buffer[pos++]);
        }
        return !sep.toString().equals(separator);
    }

    String next() throws IOException {
//        checkIsClosed();
        while (true) {
            fillBuffer();
            if (pos >= end || checker.isGoodChar(buffer[pos])) {
                break;
            }
            ++pos;
        }
        StringBuilder word = new StringBuilder();
        while (true) {
            fillBuffer();
            if (pos >= end || endsWithSep(word) || !checker.isGoodChar(buffer[pos])) {
                break;
            }
            word.append(buffer[pos++]);
        }
        if (word.isEmpty())
            return null;
        return word.toString();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(next());
    }
}
