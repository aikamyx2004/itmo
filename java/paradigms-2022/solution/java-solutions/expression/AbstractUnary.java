package expression;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

public abstract class AbstractUnary implements EveryExpression {
    protected final EveryExpression expression;
    protected final String operation;
    protected final IntUnaryOperator integerOperator;

    protected AbstractUnary(
            EveryExpression expression,
            String operation,
            IntUnaryOperator integerOperator
    ) {
        this.expression = expression;
        this.operation = operation;
        this.integerOperator = integerOperator;
    }

    @Override
    public int evaluate(int x) {
        return integerOperator.applyAsInt(expression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return integerOperator.applyAsInt(expression.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        fillString(result);
        return result.toString();
    }


    @Override
    public void fillString(StringBuilder result) {
        result.append(operation);
        result.append('(');
        expression.fillString(result);
        result.append(')');
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        fillMiniString(sb);
        return sb.toString();
    }

    @Override
    public void fillMiniString(StringBuilder result) {
        result.append(operation);
        if (expression instanceof AbstractBinary) {
            result.append('(');
        } else {
            result.append(' ');
        }
        expression.fillMiniString(result);
        if (expression instanceof AbstractBinary) {
            result.append(')');
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUnary that = (AbstractUnary) o;
        return integerOperator.equals(that.integerOperator) && expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, integerOperator);
    }
}
