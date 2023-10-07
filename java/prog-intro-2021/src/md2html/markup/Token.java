package md2html.markup;

import java.util.function.Function;

public interface Token extends Function<String, Token> {
    void toHTML(StringBuilder result);
}
