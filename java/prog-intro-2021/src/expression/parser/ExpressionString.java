package expression.parser;

import java.util.HashMap;
import java.util.Map;

public class ExpressionString {
    private final Map<Integer, Integer> brackets;
    private final String string;
    private int begin, end;

    public ExpressionString(String string) {
        this.string = string;
        begin = 0;
        end = string.length();
        brackets = new HashMap<>();
        preCalc(brackets);
    }

    private ExpressionString(ExpressionString source) {
        brackets = source.brackets;
        string = source.string;
        begin = 0;
        end = 0;
    }

    private void preCalc(Map<Integer, Integer> brackets) {
        Map<Integer, Integer> lastInLevel = new HashMap<>();
        for (int i = 0, level = 0; i < string.length(); i++) {
            if (charAt(i) == '(') {
                lastInLevel.put(level, i);
                level++;
            } else if (charAt(i) == ')') {
                level--;
                brackets.put(i, lastInLevel.get(level));
                brackets.put(lastInLevel.get(level), i);
            }
        }
    }

    public ExpressionString substring(int begin, int end) {
        ExpressionString source = new ExpressionString(this);
        source.begin = begin + this.begin;
        source.end = end + this.begin;
        return source;
    }

    public ExpressionString substring(int begin) {
        return substring(begin, end - this.begin);
    }

    public char charAt(int index) {
        return string.charAt(begin + index);
    }

    public int length() {
        return end - begin;
    }

    public boolean startsWith(String t, int index) {
        return string.startsWith(t, begin + index);
    }

    public boolean startsWith(String t) {
        return startsWith(t, 0);
    }

    @Override
    public String toString() {
        return string.substring(begin, end);
    }

    public boolean isEmpty() {
        return begin >= end || string.isEmpty();
    }

    public boolean isGoodBracket(int index) {
        return begin + index < end && charAt(index) == '(' && brackets.get(begin + index) == end - 1;
    }

    public void deleteBrackets() {
        while (isGoodBracket(0)) {
            ++begin;
            --end;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        String that = (String) o;
        return string.substring(begin, end).equals(that);
    }

}
