package expression;

public class Min extends AbstractBinary {
    public Min(final EveryExpression first, final EveryExpression second) {
        super(first, second, "min", Integer::min);
    }

    @Override
    public boolean checkSecondBrackets() {
        return second instanceof Max;
    }
}
