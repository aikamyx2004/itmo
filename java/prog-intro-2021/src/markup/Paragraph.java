package markup;

import java.util.List;

public class Paragraph extends MarkupList implements Item {
    public Paragraph(List<Token> data) {
        super(data);
    }

    public void toMarkdown(StringBuilder result) {
        super.toMarkdown(result);
    }

    @Override
    public void toBBCode(StringBuilder result) {
        super.toBBCode(result);
    }
}
