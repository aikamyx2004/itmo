package expression;

import expression.exceptions.Log;
import expression.exceptions.Power;

public class Multiply extends AbstractBinary {
    public Multiply(final EveryExpression first, final EveryExpression second) {
        super(first, second, "*", (a, b) -> a * b);
    }

    @Override
    public boolean checkFirstBrackets() {
        return (first instanceof AbstractBinary && !(first instanceof Multiply || first instanceof Divide || first instanceof Power || first instanceof Log));
    }

    @Override
    public boolean checkSecondBrackets() {
        return second instanceof AbstractBinary && !(second instanceof Multiply || second instanceof Power || second instanceof Log);
    }
}
