package md2html.markup;

import java.util.function.Function;

public class Emphasis extends Markup implements Function<String, Token> {
    public Emphasis(String text) {
        super(text);
    }

    @Override
    public void toHTML(StringBuilder result) {
        super.toHTML(result, "em");
    }

    @Override
    public Emphasis apply(String s) {
        return new Emphasis(s);
    }
}