package expression.exceptions;

import expression.EveryExpression;

import java.util.function.BiFunction;

public class BinaryTriple implements Comparable<BinaryTriple> {
    private final String operation;
    private final int priority;
    private final BiFunction<EveryExpression, EveryExpression, EveryExpression> function;

    protected BinaryTriple(String operation, int priority, BiFunction<EveryExpression, EveryExpression, EveryExpression> function) {
        this.operation = operation;
        this.priority = priority;
        this.function = function;
    }

    public BiFunction<EveryExpression, EveryExpression, EveryExpression> getFunction() {
        return function;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public int compareTo(BinaryTriple o) {
        return Integer.compare(priority, o.priority);
    }

}
