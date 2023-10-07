package md2html.markup;

public class Text implements Token {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    public void toHTML(StringBuilder result) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '<') {
                result.append("&lt;");
            } else if (c == '>') {
                result.append("&gt;");
            } else if (c == '&') {
                result.append("&amp;");
            } else {
                if (c == '\\')
                    continue;
                result.append(c);
            }
        }
    }

    @Override
    public Token apply(String s) {
        return new Text(s);
    }
}
