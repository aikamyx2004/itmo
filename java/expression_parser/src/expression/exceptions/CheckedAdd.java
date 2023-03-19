package expression.exceptions;

import expression.Add;
import expression.EveryExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(EveryExpression first, EveryExpression second) {
        super(first, second);
    }


    @Override
    public int evaluate(int x, int y, int z) {
        int left = first.evaluate(x, y, z);
        int right = second.evaluate(x, y, z);
        if (left > 0 && right > Integer.MAX_VALUE - left || left < 0 && right < Integer.MIN_VALUE - left) {
            throw new OverflowException("Overflow in " + left + " * " + right);
        }
        return left + right;
    }
}
