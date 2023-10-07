package expression.exceptions;

import expression.EveryExpression;

import java.util.function.Function;

public class UnaryTriple {
    private final String operation;
    private final Function<EveryExpression, EveryExpression> function;
    private final boolean needBrackets;

    public UnaryTriple(String operation, boolean needBrackets, Function<EveryExpression, EveryExpression> function) {
        this.operation = operation;
        this.needBrackets = needBrackets;
        this.function = function;
    }

    public Function<EveryExpression, EveryExpression> getFunction() {
        return function;
    }

    public boolean isNeedBrackets() {
        return needBrackets;
    }

    public String getOperation() {
        return operation;
    }
}
