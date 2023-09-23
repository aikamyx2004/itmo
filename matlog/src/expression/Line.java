package expression;

import java.util.*;

public class Line implements Expression {
    private final ContextList context;
    private final Expression expression;
    private final int order;

    public Line(ContextList context, Expression expression) {
        order = 0;
        this.context = context;
        this.expression = expression;
    }
    public Line(ContextList context, Expression expression, int order) {
        this.order = order;
        this.context = context;
        this.expression = expression;
    }

    @Override
    public String parseTree() {
        return null;
    }

    @Override
    public Line copy() {
        return new Line(context.copy(), expression.copy(), order);
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
    public String toString() {
        return String.format("%s|-%s", context.toString(), expression.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(context, line.context) && Objects.equals(expression, line.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(1, context, expression);
    }

    public int getOrder() {
        return order;
    }

    public Expression getExpression() {
        return expression;
    }

    public ContextList getContext() {
        return context;
    }
}
