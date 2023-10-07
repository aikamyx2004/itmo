package expression;

import java.math.BigInteger;
import java.util.Objects;

public class Const implements EveryExpression {
    private final BigInteger number;

    public Const(final int number) {
        this.number = BigInteger.valueOf(number);
    }

    public Const(final BigInteger number) {
        this.number = number;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return number.intValue();
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    public BigInteger evaluate(BigInteger x) {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Const that = (Const) o;
        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public int evaluate(int x) {
        return number.intValue();
    }
}
