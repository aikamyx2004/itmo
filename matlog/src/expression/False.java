package expression;

import java.util.*;

public class False implements Expression {

    @Override
    public String parseTree() {
        return toString();
    }

    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return false;
    }

    @Override
    public void fillVariables(Map<String, Boolean> variables) {
        // no operation
    }

    @Override
    public boolean fillEvaluation(Map<Expression, Boolean> exprEvaluation, Map<String, Boolean> evaluation) {
        exprEvaluation.put(this, false);
        return false;
    }

    @Override
    public Expression copy() {
        return new False();
    }

    @Override
    public String toString() {
        return "_|_";
    }
}
