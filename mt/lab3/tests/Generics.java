import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Generics {
    private final List<Map<Integer, String>> list = List.of(Map.of(1, String.valueOf(2)));

    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>();
        BiFunction<String, Integer, String> f;
    }
}
