package expression.exceptions;

import expression.Divide;
import expression.EveryExpression;

public class CheckedDivide extends Divide {
    public CheckedDivide(EveryExpression first, EveryExpression second) {
        super(first, second);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int left = first.evaluate(x, y, z);
        int right = second.evaluate(x, y, z);
        if (right == 0 || left == Integer.MIN_VALUE && right == -1) {
            throw new ArithmeticException("division by zero in " + left + " / " + right);
        }
        return left / right;
    }
}
