package expression;

import java.util.Objects;

public class Const implements EveryExpression {
    private final int number;

    public Const(final int number) {
        this.number = number;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return number;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Const that = (Const) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public int evaluate(int x) {
        return number;
    }
}
