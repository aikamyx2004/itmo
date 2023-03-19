package expression.exceptions;


import expression.AbstractBinary;
import expression.EveryExpression;

public class Log extends AbstractBinary {
    public Log(EveryExpression first, EveryExpression second) {
        super(first, second, "//", (a, b) -> {
            if (a <= 0 || b <= 1) {
                throw new ArithmeticException(String.format("log_%d(%d) is undefined", b, a));
            }
            int c = 0;
            while (a >= b) {
                a /= b;
                c++;
            }
            return c;
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
