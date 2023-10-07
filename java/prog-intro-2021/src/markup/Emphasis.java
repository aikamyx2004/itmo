package markup;

import java.util.List;

public class Emphasis extends MarkupList implements Token {
    public Emphasis(List<Token> data) {
        super(data);
    }

    @Override
    public void toMarkdown(StringBuilder result) {
        super.toMarkdownWithStrings(result, "*", "*");
    }

    @Override
    public void toBBCode(StringBuilder result) {
        super.toBBCodeWithStrings(result, "[i]", "[/i]");
    }

}
