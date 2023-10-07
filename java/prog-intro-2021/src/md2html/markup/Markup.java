package md2html.markup;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Markup implements Token {
    protected List<Token> data;
    private final static List<Triple> codes = List.of(
            new Triple("**", null, "strong", Strong::new),
            new Triple("__", null, "strong", Strong::new),
            new Triple("*", "**", "em", Emphasis::new),
            new Triple("_", "__", "em", Emphasis::new),
            new Triple("```", null, "pre", Pre::new),
            new Triple("`", "```", "code", Code::new),
            new Triple("--", null, "s", Strikeout::new)
    );

    public Markup(String text) {
        data = parse(text);
    }

    protected Markup() {
    }

    @Override
    public void toHTML(StringBuilder result) {
        toHTMLList(result);
    }

    public void toHTML(StringBuilder result, String code) {
        result.append("<").append(code).append(">");
        toHTMLList(result);
        result.append("</").append(code).append(">");
    }

    private void toHTMLList(StringBuilder result) {
        for (Token token : data) {
            token.toHTML(result);
        }
    }

    protected List<Token> parse(String text) {
        List<Token> result = new ArrayList<>();
        outer:
        for (int i = 0; i < text.length(); ) {
            if (Pre.isGoodStart(text, "```", null, i)) {
                int end = Pre.getEnd(text, "```", null, i);
                addElement(result, text.substring(i + 3, end), Pre::new);
                i = end + 3;
                continue;
            }

            for (Triple entry : codes) {
                String code = entry.getCode();
                String wrongCode = entry.getWrongCode();

                Function<String, ? extends Token> classOfToken = entry.getFunction();
                if (isGoodStart(text, code, wrongCode, i)) {
                    int end = getEnd(text, code, wrongCode, i);
                    addElement(result, text.substring(i + code.length(), end), classOfToken);
                    i = end + code.length();
                    continue outer;
                }
            }
            i = getText(text, i, result);
        }
        return result;
    }

    private static void addElement(List<Token> result, String text, Function<String, ? extends Token> classOfToken) {
        try {
            result.add(classOfToken.apply(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getText(String text, int begin, List<Token> result) {
        int end = begin;
        outWhile:
        while (end < text.length()) {
            for (Triple entry : codes) {
                String code = entry.getCode();
                String wrongCode = entry.getWrongCode();
                if (isGoodStart(text, code, wrongCode, end)) {
                    break outWhile;
                }
            }
            ++end;
        }
        result.add(new Text(text.substring(begin, end)));
        return end;
    }


    protected static boolean isGoodStart(String text, String code, String wrongCode, int index) {
        return text.startsWith(code, index)
                && (wrongCode == null || !text.startsWith(wrongCode, index))
                && index + code.length() < text.length()
                && checkSeparator(text.charAt(index + code.length()))
                && checkSlash(text, index - 1);
    }


    protected static int goodEnd(String text, String code, String wrongCode, int index) {
        if ((wrongCode != null && text.startsWith(wrongCode, index + 1)))
            return index + wrongCode.length();
        else if (text.startsWith(code, index + 1) && checkSeparator(text.charAt(index)) && checkSlash(text, index)) {
            return -1;
        } else {
            return index + 1;
        }
    }

    protected static int getEnd(String text, String code, String wrongCode, int begin) {
        int end = begin;
        while (end < text.length()) {
            int val = goodEnd(text, code, wrongCode, end);
            if (val == -1) {
                break;
            } else {
                end = val;
            }
        }
        return Math.min(text.length(), end + 1);
    }

    protected static boolean checkSlash(String text, int index) {
        if (index < 0)
            return true;
        return text.charAt(index) != '\\';
    }

    protected static boolean checkSeparator(char c) {
        return !Character.isWhitespace(c) && System.lineSeparator().indexOf(c) == -1;
    }
}
