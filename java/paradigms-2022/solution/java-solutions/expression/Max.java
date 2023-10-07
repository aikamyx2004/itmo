package expression;

public class Max extends AbstractBinary {
    public Max(final EveryExpression first, final EveryExpression second) {
        super(first, second, "max", Integer::max);
    }

    @Override
    public boolean checkSecondBrackets() {
        return second instanceof Min;
    }
}
