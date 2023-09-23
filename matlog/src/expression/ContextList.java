package expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ContextList implements Expression {
    private final List<Expression> expressions;

    public ContextList(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public ContextList copy() {
        return new ContextList(new ArrayList<>(expressions));
    }

    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return false;
    }

    @Override
    public void fillVariables(Map<String, Boolean> variables) {

    }

    @Override
    public boolean fillEvaluation(Map<Expression, Boolean> exprEvaluation, Map<String, Boolean> evaluation) {
        return false;
    }

    @Override
    public String parseTree() {
        return expressions.stream().map(Expression::parseTree).collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        if(expressions == null){
            return "";
        }
        return expressions.stream().map(Expression::toString).collect(Collectors.joining(","));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContextList that = (ContextList) o;
        return Objects.equals(expressions, that.expressions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressions);
    }

    public List<Expression> getExpressions() {
        return expressions;
    }
}
