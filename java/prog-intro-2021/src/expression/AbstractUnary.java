package expression;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

public abstract class AbstractUnary implements EveryExpression {
    private final EveryExpression expression;
    private final String operation;
    private final UnaryOperator<BigInteger> bigIntegerOperator;
    private final IntUnaryOperator integerOperator;
    private final int precacheHash;

    protected AbstractUnary(
            EveryExpression expression,
            String operation,
            IntUnaryOperator integerOperator,
            UnaryOperator<BigInteger> bigIntegerOperator
    ) {
        this.expression = expression;
        this.operation = operation;
        this.integerOperator = integerOperator;
        this.bigIntegerOperator = bigIntegerOperator;
        precacheHash = Objects.hash(expression, operation);
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
    public BigInteger evaluate(BigInteger x) {
        return bigIntegerOperator.apply(expression.evaluate(x));
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
        return precacheHash == that.precacheHash && expression.equals(that.expression) && operation.equals(operation);
    }

    @Override
    public int hashCode() {
        return precacheHash;
    }
}
