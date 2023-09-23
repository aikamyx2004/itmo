package expression;


public class Conjunction extends BinaryOperation {
    public Conjunction(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String getOperationSign() {
        return "&";
    }

    @Override
    public boolean operation(boolean left, boolean right) {
        return left&right;
    }

    @Override
    public Conjunction copy() {
        return new Conjunction(getLeft().copy(), getRight().copy());
    }
}
