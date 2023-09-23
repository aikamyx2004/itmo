package expression;

public class Implication extends BinaryOperation {
    public Implication(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String getOperationSign() {
        return "->";
    }

    @Override
    public boolean operation(boolean left, boolean right) {
        return (!left) | right;
    }

    @Override
    public Expression copy() {
        return new Implication(getLeft().copy(), getRight().copy());
    }
}
