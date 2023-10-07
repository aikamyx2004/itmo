package markup;

import java.util.List;

public abstract class BBCodeList {
    protected List<Token> data;

    protected BBCodeList(List<Token> data) {
        this.data = data;
    }

    public void toBBCode(StringBuilder result) {
        toBBCodeList(result);
    }

    protected void toBBCodeWithStrings(StringBuilder result, String head, String tail) {
        result.append(head);
        toBBCodeList(result);
        result.append(tail);
    }

    private void toBBCodeList(StringBuilder result) {
        for (Token token : data) {
            token.toBBCode(result);
        }
    }

}
