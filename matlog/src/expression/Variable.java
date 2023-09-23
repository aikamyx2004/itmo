package expression;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class Variable implements Expression {
    private final String variable;
    private Integer hash;

    public Variable(String variable) {
        this.variable = variable;
    }

    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return value.get(variable);
    }

    @Override
    public void fillVariables(Map<String, Boolean> variables) {
        variables.put(variable, false);
    }

    @Override
    public boolean fillEvaluation(Map<Expression, Boolean> exprEvaluation, Map<String, Boolean> evaluation) {
        boolean res = evaluation.get(variable);
        exprEvaluation.put(this, res);
        return res;
    }

    @Override
    public String parseTree() {
        return variable;
    }

    @Override
    public Expression copy() {
        return new Variable(variable);
    }

    public String getVariable() {
        return variable;
    }

    @Override
    public String toString() {
        return variable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable1 = (Variable) o;
        return Objects.equals(variable, variable1.variable);
    }

    @Override
    public int hashCode() {
        if (hash == null) {
            hash = Objects.hash(variable);
        }
        return hash;
    }
}
