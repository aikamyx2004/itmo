package expression;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Negation implements Expression {
    private final Expression expr;
    private Integer hash;

    public Negation(Expression expr) {
        this.expr = expr;
    }

    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return !expr.evaluate(value);
    }

    @Override
    public String parseTree() {
        return String.format("(!%s)", expr.parseTree());
    }

    @Override
    public String toString() {
        return String.format("(!%s)", expr.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Negation negation = (Negation) o;
        return Objects.equals(expr, negation.expr);
    }

    @Override
    public void fillVariables(Map<String, Boolean> variables) {
        expr.fillVariables(variables);
    }

    @Override
    public boolean fillEvaluation(Map<Expression, Boolean> exprEvaluation, Map<String, Boolean> evaluation) {
        return !expr.fillEvaluation(exprEvaluation, evaluation);
    }

    @Override
    public Expression copy() {
        return new Negation(expr.copy());
    }

    @Override
    public int hashCode() {
        if (hash == null) {
            hash = Objects.hash(-1, expr);
        }
        return hash;
    }

    public Expression getExpr() {
        return expr;
    }
}
