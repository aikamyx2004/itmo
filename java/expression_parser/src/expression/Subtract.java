package expression;


import expression.exceptions.Log;
import expression.exceptions.Power;

public class Subtract extends AbstractBinary {
    public Subtract(final EveryExpression first, final EveryExpression second) {
        super(first, second, "-", (a, b) -> a - b);
    }

    @Override
    public boolean checkFirstBrackets() {
        return first.getClass() == Min.class || first.getClass() == Max.class;
    }

    @Override
    public boolean checkSecondBrackets() {
        return second instanceof AbstractBinary &&
                !(second instanceof Multiply || second instanceof Divide || second instanceof Power || second instanceof Log);
    }
}
