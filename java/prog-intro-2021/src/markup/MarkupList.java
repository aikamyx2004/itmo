package markup;

import java.util.List;

public abstract class MarkupList extends BBCodeList {
    protected MarkupList(List<Token> data) {
        super(data);
    }

    public void toMarkdown(StringBuilder result) {
        toMarkdownList(result);
    }

    protected void toMarkdownWithStrings(StringBuilder result, String head, String tail) {
        result.append(head);
        toMarkdownList(result);
        result.append(tail);
    }

    private void toMarkdownList(StringBuilder result) {
        for (Token token : data) {
            token.toMarkdown(result);
        }
    }
}
