package expression.exceptions;

import expression.EveryExpression;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(EveryExpression first, EveryExpression second) {
        super(first, second);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int left = first.evaluate(x, y, z);
        int right = second.evaluate(x, y, z);

        if (checkOverflow(left, right)) {
            throw new ExpressionExceptions.OverflowException("Overflow in " + left + " * " + right);
        }
        return left * right;
    }

    private boolean checkOverflow(int a, int b) {
        if (a == 0 || b == 0) {
            return false;
        }
        if (a > b) {
            int c = a;
            a = b;
            b = c;
        }
        if (a > 0) {
            return a > Integer.MAX_VALUE / b || b > Integer.MAX_VALUE / a;
        } else if (b > 0) {
            return a < Integer.MIN_VALUE / b || (b > Integer.MIN_VALUE / a && a != -1);
        } else {
            return a < Integer.MAX_VALUE / b || b < Integer.MAX_VALUE / a;
        }
    }
}
