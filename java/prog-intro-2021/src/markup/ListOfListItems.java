package markup;

import java.util.List;

public abstract class ListOfListItems implements Item {
    private final List<ListItem> data;

    protected ListOfListItems(List<ListItem> data) {
        this.data = data;
    }

    protected void toBBCode(StringBuilder result, String head) {
        result.append(head);
        for (ListItem element : data) {
            element.toBBCode(result);
        }
        result.append("[/list]");
    }
}
