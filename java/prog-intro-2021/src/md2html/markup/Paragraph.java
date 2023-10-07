package md2html.markup;

import java.util.List;

public class Paragraph extends Markup {
    private int level;

    public Paragraph(String text) {
        data = parse(text);
    }

    @Override
    protected List<Token> parse(String text) {
        int count = 0;
        while (count < text.length() && text.charAt(count) == '#') {
            ++count;
        }
        level = count == 0 || !Character.isWhitespace(text.charAt(count)) ? -1 : count;
        return super.parse(text.substring(level + 1));
    }

    @Override
    public void toHTML(StringBuilder result) {
        if (1 <= level && level <= 6) {
            super.toHTML(result, "h" + level);
        } else {
            super.toHTML(result, "p");
        }
    }

    @Override
    public Token apply(String s) {
        return new Paragraph(s);
    }
}
