package ru.ainur.obfuscator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObfuscatorUtil {
    private static final char[][] SYMBOLS = new char[][]{{'1', 'I'}, {'0', 'O'}};

    public static Map<List<String>, Map<String, String>> getNewNames(Map<List<String>, List<String>> variables) {
        List<String> names = generateNewNames(getSummedLengths(variables));
        Map<List<String>, Map<String, String>> newNames = new HashMap<>();

        int index = 0;
        for (var e : variables.entrySet()) {
            newNames.putIfAbsent(e.getKey(), new HashMap<>());
            for (String name : e.getValue()) {
                String newName = names.get(index++);
                newNames.get(e.getKey())
                        .put(name, newName);
            }
        }
        return newNames;
    }

    private static List<String> generateNewNames(int count) {
        int base = getBase(count);
        List<String> names = new ArrayList<>();
        for (int i = 0; names.size() < count; i++) {
            String name = generateName(base, i);
            names.add(name);
        }
        return names;
    }

    private static String generateName(int base, int i) {
        StringBuilder result = new StringBuilder();
        result.append('I');

        for (int j = 1; j <= base; j++) {
            result.append(SYMBOLS[j % 2][(i >> (j - 1)) & 1]);
        }
        return result.toString();
    }

    private static int getSummedLengths(Map<List<String>, List<String>> variables) {
        return variables.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    private static int getBase(int n) {
        int c = 0;
        while (n > 0) {
            n /= 2;
            c++;
        }
        return c;
    }
}
