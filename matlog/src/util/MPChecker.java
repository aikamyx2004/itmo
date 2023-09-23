package util;

import expression.Expression;
import expression.Implication;
import expression.Line;
import expression.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MPChecker {
    private static final List<Pair<Expression, Integer>> EMPTY_LIST = new ArrayList<>();
    private final Map<Expression, Integer> index = new HashMap<>();
    private final Map<Expression, List<Pair<Expression, Integer>>> possibleMP = new HashMap<>();
    private final Map<Expression, Pair<Integer, Integer>> MPIndexes = new HashMap<>();

    public void update(Line line) {
        Expression expr = line.getExpression();
        if (index.get(expr) != null) {
            return;
        }
        index.put(expr, line.getOrder());


        if (expr instanceof Implication) {
            Implication impl = (Implication) expr;
            Expression left = impl.getLeft();
            Integer ind = index.get(left);
            if (ind != null) {
                MPIndexes.put(impl.getRight(), new Pair<>(line.getOrder(), ind));
            } else {
                Expression right = impl.getRight();
                possibleMP.putIfAbsent(left, new ArrayList<>());
                possibleMP.get(left).add(new Pair<>(right, line.getOrder()));
            }
        }
        for (Pair<Expression, Integer> pair : possibleMP.getOrDefault(expr, EMPTY_LIST)) {
            MPIndexes.put(pair.a, new Pair<>(pair.b, line.getOrder()));
        }
        possibleMP.remove(expr);
    }

    public Pair<Integer, Integer> check(Expression expr) {
        return MPIndexes.get(expr);
    }

}
