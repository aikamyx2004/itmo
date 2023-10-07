package expression;

public class Add extends AbstractBinary {
    public Add(final EveryExpression first, final EveryExpression second) {
        super(first, second, "+", Integer::sum);
    }

    @Override
    public boolean checkFirstBrackets() {
        return first.getClass() == Min.class || first.getClass() == Max.class;
    }

    @Override
    public boolean checkSecondBrackets() {
        return second.getClass() == Max.class || second.getClass() == Min.class;
    }

}
