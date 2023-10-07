package md2html.markup;

public class Pre extends Markup {
    String text;

    public Pre(String text) {
        this.text = text;
    }

    @Override
    public void toHTML(StringBuilder result) {
        result.append("<pre>").append(text).append("</pre>");
    }

    protected static boolean isGoodStart(String text, String code, String wrongCode, int index) {
        return text.startsWith(code, index)
                && checkSlash(text, index - 1);
    }

    protected static int goodEnd(String text, String code, String wrongCode, int index) {
        if ((wrongCode != null && text.startsWith(wrongCode, index + 1)))
            return index + wrongCode.length();
        else if (text.startsWith(code, index + 1) && checkSlash(text, index)) {
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

    @Override
    public Token apply(String s) {
        return new Pre(s);
    }
}