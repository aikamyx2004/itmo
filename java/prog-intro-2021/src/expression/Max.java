package expression;

import java.math.BigInteger;

public class Max extends AbstractBinary {
    public Max(final EveryExpression first, final EveryExpression second) {
        super(first, second, "max", BigInteger::max, Integer::max);
    }
}
