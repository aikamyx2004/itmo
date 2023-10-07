package expression;

import java.math.BigInteger;

public class Min extends AbstractBinary {
    public Min(final EveryExpression first, final EveryExpression second) {
        super(first, second, "min", BigInteger::min, Integer::min);
    }
}
