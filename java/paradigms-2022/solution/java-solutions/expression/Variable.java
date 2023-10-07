package expression;

import java.util.Objects;

public class Variable implements EveryExpression {
    private final String variable;

    public Variable(final String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return variable;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (variable) {
            case ("x") -> x;
            case ("y") -> y;
            default -> z;
        };
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Variable that = (Variable) o;
        return variable.equals(that.variable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable);
    }

}
