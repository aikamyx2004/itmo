package expression;

public class Negate extends AbstractUnary {
    public Negate(EveryExpression expression) {
        super(expression, "-", a -> -a);
    }
}
