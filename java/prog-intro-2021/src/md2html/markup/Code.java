package md2html.markup;

import java.util.function.Function;

public class Code extends Markup implements Function<String, Token> {
    public Code(String text) {
        super(text);
    }

    @Override
    public void toHTML(StringBuilder result) {
        super.toHTML(result, "code");
    }

    @Override
    public Code apply(String s) {
        return new Code(s);
    }
}