package markup;

import java.util.List;

public class OrderedList extends ListOfListItems {
    public OrderedList(List<ListItem> data) {
        super(data);
    }

    @Override
    public void toBBCode(StringBuilder result) {
        super.toBBCode(result, "[list=1]");
    }
}
