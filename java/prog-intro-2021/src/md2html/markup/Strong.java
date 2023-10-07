package md2html.markup;

import java.util.function.Function;

public class Strong extends Markup implements Function<String, Token> {
    public Strong(String text) {
        super(text);
    }

    @Override
    public void toHTML(StringBuilder result) {
        super.toHTML(result, "strong");
    }

    @Override
    public Strong apply(String s) {
        return new Strong(s);
    }
}