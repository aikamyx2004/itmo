package expression;

import expression.TripleExpression;

@FunctionalInterface
public interface TripleParser {
    TripleExpression parse(String expression);
}
