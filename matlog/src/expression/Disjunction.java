package expression;

import java.util.Map;

public class Disjunction extends BinaryOperation {

    public Disjunction(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String getOperationSign() {
        return "|";
    }

    @Override
    public boolean operation(boolean left, boolean right) {
        return left | right;
    }

    @Override
    public Expression copy() {
        return new Disjunction(getLeft().copy(), getRight().copy());
    }
}
