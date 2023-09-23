//package expression;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//public class ContextSet implements Expression{
//    private final HashMap<Expression, Integer> expressions;
//
//    public ContextSet(List<Expression> expressions) {
//        this.expressions = new HashMap<>();
//        for (Expression expr : expressions) {
//            this.expressions.put(expr, this.expressions.getOrDefault(expr, 0) + 1);
//        }
//    }
//
//    @Override
//    public Expression copy() {
//        return new Con;
//    }
//
//    @Override
//    public String parseTree() {
//        return expressions.keySet().stream().map(s -> s.parseTree().repeat(expressions.get(s))).collect(Collectors.joining(", "));
//    }
//
//    @Override
//    public String toString() {
//        return expressions.keySet().stream().map(s -> s.toString().repeat(expressions.get(s))).collect(Collectors.joining(", "));
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ContextSet that = (ContextSet) o;
//        return Objects.equals(expressions, that.expressions);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(expressions);
//    }
//
//    public HashMap<Expression, Integer> getExpressions() {
//        return expressions;
//    }
//}
