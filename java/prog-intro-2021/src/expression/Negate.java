package expression;

import java.math.BigInteger;

public class Negate extends AbstractUnary {
    public Negate(EveryExpression expression) {
        super(expression, "-", a -> -a, bigInteger -> bigInteger.multiply(BigInteger.valueOf(-1)));
    }
}
