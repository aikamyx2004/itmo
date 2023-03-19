package expression.exceptions;


import expression.AbstractBinary;
import expression.Const;
import expression.EveryExpression;

public class Power extends AbstractBinary {
    public Power(EveryExpression first, EveryExpression second) {
        super(first, second, "**", (a, b) -> {
            if (b == 0) {
                if (a == 0) {
                    throw new ArithmeticException("0^0 is undefined");
                }
                return 1;
            }
            if (b < 0) {
                throw new ArithmeticException(String.format("%d^%d is undefined", a, b));
            }
            int c = 1;
            while (true) {
                if (b % 2 == 1) {
                    // :NOTE: Insufficient check: temporary object created
                    c = new CheckedMultiply(new Const(c), new Const(a)).evaluate(0, 0, 0);
                }
                if (b == 1) {
                    return c;
                }
                a = new CheckedMultiply(new Const(a), new Const(a)).evaluate(0, 0, 0);
                b /= 2;
            }
        });
    }

    @Override
    public boolean checkFirstBrackets() {
        return (first instanceof AbstractBinary) && !(first instanceof Power || first instanceof Log);
    }

    @Override
    public boolean checkSecondBrackets() {
        return (second instanceof AbstractBinary);
    }
}
