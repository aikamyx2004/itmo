package markup;

import java.util.List;

public class ListItem implements BBCode {
    private final List<Item> data;

    public ListItem(List<Item> data) {
        this.data = data;
    }

    @Override
    public void toBBCode(StringBuilder result) {
        result.append("[*]");
        for (Item item : data) {
            item.toBBCode(result);
        }
    }
}
