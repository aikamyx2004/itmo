package markup;

import java.util.List;

public class Strikeout extends MarkupList implements Token {
    public Strikeout(List<Token> data) {
        super(data);
    }

    @Override
    public void toMarkdown(StringBuilder result) {
        super.toMarkdownWithStrings(result, "~", "~");
    }

    @Override
    public void toBBCode(StringBuilder result) {
        super.toBBCodeWithStrings(result, "[s]", "[/s]");
    }
}
