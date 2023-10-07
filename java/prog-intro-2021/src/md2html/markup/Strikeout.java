package md2html.markup;

import java.util.function.Function;

public class Strikeout extends Markup implements Function<String, Token> {
    public Strikeout(String text) {
        super(text);
    }

    @Override
    public void toHTML(StringBuilder result) {
        super.toHTML(result, "s");
    }

    @Override
    public Strikeout apply(String s) {
        return new Strikeout(s);
    }
}