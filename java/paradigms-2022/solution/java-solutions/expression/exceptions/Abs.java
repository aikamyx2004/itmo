package expression.exceptions;


import expression.AbstractUnary;
import expression.EveryExpression;

public class Abs extends AbstractUnary {
    public Abs(EveryExpression first) {
        super(first, "abs", a -> {
            if (a == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow abs(" + Integer.MIN_VALUE + ")");
            }
            return (a < 0 ? -a : a);
        });
    }

}
