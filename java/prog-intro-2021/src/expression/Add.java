package expression;

import java.math.BigInteger;

public class Add extends AbstractBinary {
    public Add(final EveryExpression first, final EveryExpression second) {
        super(first, second, "+", BigInteger::add, Integer::sum);
    }

    @Override
    public boolean checkFirstBrackets() {
        return first.getClass() == Min.class || first.getClass() == Max.class;
    }

    @Override
    public boolean checkSecondBrackets() {
        return second.getClass() == Max.class || second.getClass() == Min.class;
    }

}
