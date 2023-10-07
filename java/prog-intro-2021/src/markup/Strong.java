package markup;

import java.util.List;

public class Strong extends MarkupList implements Token {
    public Strong(List<Token> data) {
        super(data);
    }

    @Override
    public void toMarkdown(StringBuilder result) {
        super.toMarkdownWithStrings(result, "__", "__");
    }

    @Override
    public void toBBCode(StringBuilder result) {
        super.toBBCodeWithStrings(result, "[b]", "[/b]");
    }
}
