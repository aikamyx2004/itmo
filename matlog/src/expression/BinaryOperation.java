package expression;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class BinaryOperation implements Expression {
    private final Expression left;
    private final Expression right;
    private Integer hash;

    private String calculatedString;

    public BinaryOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public abstract String getOperationSign();

    public abstract boolean operation(boolean left, boolean right);

    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return operation(left.evaluate(value), right.evaluate(value));
    }

    @Override
    public void fillVariables(Map<String, Boolean> variables) {
        left.fillVariables(variables);
        right.fillVariables(variables);
    }

    @Override
    public boolean fillEvaluation(Map<Expression, Boolean> exprEvaluation, Map<String, Boolean> evaluation) {
        boolean l = left.fillEvaluation(exprEvaluation, evaluation);
        boolean r = right.fillEvaluation(exprEvaluation, evaluation);
        boolean res = operation(l, r);
        exprEvaluation.put(this, res);
        return res;
    }

    @Override
    public String parseTree() {
        return String.format("(%s,%s,%s)", getOperationSign(), left.parseTree(), right.parseTree());
    }

    @Override
    public String toString() {
        if (calculatedString == null) {
            return calculatedString = String.format("(%s%s%s)", left.toString(), getOperationSign(), right.toString());
        }
        return calculatedString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryOperation that = (BinaryOperation) o;
        return Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        if (hash == null) {
            hash = Objects.hash(getOperationSign(), left, right);
        }
        return hash;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}
