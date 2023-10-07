package expression;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;


public abstract class AbstractBinary implements EveryExpression {
    protected final EveryExpression first;
    protected final EveryExpression second;
    private final String operation;
    private final BinaryOperator<BigInteger> bigIntegerOperator;
    private final IntBinaryOperator integerOperator;
    private final int precacheHash;

    protected AbstractBinary(
            final EveryExpression first, final EveryExpression second,
            final String operation,
            final BinaryOperator<BigInteger> bigIntegerOperator,
            final IntBinaryOperator integerOperator
    ) {
        this.first = first;
        this.second = second;
        this.operation = operation;
        this.bigIntegerOperator = bigIntegerOperator;
        this.integerOperator = integerOperator;
        precacheHash = Objects.hash(first, second, operation);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return integerOperator.applyAsInt(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    @Override
    public int evaluate(int x) {
        return integerOperator.applyAsInt(first.evaluate(x), second.evaluate(x));
    }

    @Override
    public BigInteger evaluate(BigInteger x) {
        return bigIntegerOperator.apply(first.evaluate(x), second.evaluate(x));
    }

    private void fillOperation(StringBuilder result) {
        result.append(' ').append(operation).append(' ');
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        fillString(result);
        return result.toString();
    }

    @Override
    public void fillString(final StringBuilder result) {
        result.append('(');
        first.fillString(result);
        fillOperation(result);
        second.fillString(result);
        result.append(')');
    }


    @Override
    public void fillMiniString(final StringBuilder result) {
        first.putBrackets(result, checkFirstBrackets());
        fillOperation(result);
        second.putBrackets(result, checkSecondBrackets());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBinary that = (AbstractBinary) o;
        return operation.equals(that.operation) &&
                first.equals(that.first) &&
                second.equals(that.second);
    }

    @Override
    public int hashCode() {
        return precacheHash;
    }
}
