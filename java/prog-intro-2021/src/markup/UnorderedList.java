package markup;

import java.util.List;

public class UnorderedList extends ListOfListItems {
    public UnorderedList(List<ListItem> data) {
        super(data);
    }

    @Override
    public void toBBCode(StringBuilder result) {
        super.toBBCode(result, "[list]");
    }
}
