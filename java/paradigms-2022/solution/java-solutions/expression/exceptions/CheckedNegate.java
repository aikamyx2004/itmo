package expression.exceptions;

import expression.EveryExpression;
import expression.Negate;

public class CheckedNegate extends Negate {
    public CheckedNegate(EveryExpression first) {
        super(first);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int value = expression.evaluate(x, y, z);
        if (value == Integer.MIN_VALUE) {
            throw new ExpressionExceptions.OverflowException("Overflow in -(" + value + ")");
        }
        return -value;
    }
}
