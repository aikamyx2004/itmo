package expression.exceptions;

import expression.EveryExpression;

import java.util.function.Function;

public class UnaryPair {
    private final String operation;
    private final Function<EveryExpression, EveryExpression> function;

    protected UnaryPair(String operation, Function<EveryExpression, EveryExpression> function) {
        this.operation = operation;
        this.function = function;
    }

    public Function<EveryExpression, EveryExpression> getFunction() {
        return function;
    }


    public String getOperation() {
        return operation;
    }
}
