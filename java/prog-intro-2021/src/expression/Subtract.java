package expression;

import java.math.BigInteger;

public class Subtract extends AbstractBinary {
    public Subtract(final EveryExpression first, final EveryExpression second) {
        super(first, second, "-", BigInteger::subtract, (a, b) -> a - b);
    }

    @Override
    public boolean checkFirstBrackets() {
        return first.getClass() == Min.class || first.getClass() == Max.class;
    }

    @Override
    public boolean checkSecondBrackets() {
        return second instanceof AbstractBinary &&
                !(second.getClass() == Multiply.class || second.getClass() == Divide.class);
    }
}
