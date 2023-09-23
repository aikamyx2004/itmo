package expression;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface Expression {
    String parseTree();
    Expression copy();

    boolean evaluate(Map<String, Boolean> value);

    void fillVariables(Map<String, Boolean> variables);

    boolean fillEvaluation(Map<Expression, Boolean> exprEvaluation, Map<String, Boolean> evaluation);
}
