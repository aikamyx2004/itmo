package parser;

import expression.Expression;

public class ExpressionParser {

    public Expression parse(String expr) {
        return new Parser(expr).parse();
    }


}
