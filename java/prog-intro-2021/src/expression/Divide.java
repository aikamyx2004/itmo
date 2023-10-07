package expression;

import java.math.BigInteger;

public class Divide extends AbstractBinary {
    public Divide(final EveryExpression first, final EveryExpression second) {
        super(first, second, "/", BigInteger::divide, (a, b) -> a / b);
    }

    @Override
    public boolean checkFirstBrackets() {
        return first instanceof AbstractBinary &&
                !(first.getClass() == Multiply.class || first.getClass() == Divide.class);
    }

    @Override
    public boolean checkSecondBrackets() {
        return second instanceof AbstractBinary;
    }
}
