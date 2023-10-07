package expression.parser;

import expression.*;

import java.math.BigInteger;
import java.util.List;

public class ExpressionParser implements Parser {
    private static final List<BinaryTriple> binary = List.of(
            new BinaryTriple("+", -200, Add::new),
            new BinaryTriple("-", -200, Subtract::new),
            new BinaryTriple("*", 301, Multiply::new),
            new BinaryTriple("/", 300, Divide::new),
            new BinaryTriple("max", 41, Max::new),
            new BinaryTriple("min", 41, Min::new)
    );
    private static final List<UnaryPair> unary = List.of(
            new UnaryPair("-", Negate::new),
            new UnaryPair("l0", LeftZeroes::new),
            new UnaryPair("t0", RightZeroes::new)
    );

    @Override
    public EveryExpression parse(String expression) {
        StringBuilder sb = new StringBuilder();
        for (char c : expression.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        ExpressionString source = new ExpressionString(sb.toString());
        return parseString(source);
    }

    private EveryExpression parseString(ExpressionString expression) {
        expression.deleteBrackets();
        if (checkIsNumber(expression)) {
            return new Const(takeNumber(expression.toString()));
        }

        if (isVariable(expression))
            return new Variable(expression.toString());

        for (UnaryPair triple : unary) {
            String operation = triple.getOperation();
            if (expression.startsWith(operation)) {
                if (isUnary(expression, operation.length())) {
                    expression = expression.substring(operation.length());
                    return triple.getFunction().apply(parseString(expression));
                }
            }
        }

        int pos = 0, level = 0, lastOperation = -1;
        BinaryTriple minOperation = binary.get(2);
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                do {
                    char c = expression.charAt(i);
                    if (c == '(') {
                        level++;
                    } else if (c == ')') {
                        level--;
                    }
                    i++;
                } while (level != 0 && i < expression.length());
            }

            for (BinaryTriple entry : binary) {
                String operation = entry.getOperation();
                if (expression.startsWith(operation, i)) {
                    if (minOperation.compareTo(entry) >= 0
                            && lastOperation + minOperation.getOperation().length() < i
                    ) {
                        minOperation = entry;
                        pos = i;
                    }
                    lastOperation = i;
                }
            }


        }

        ExpressionString left = expression.substring(0, pos);
        ExpressionString right = expression.substring(pos + minOperation.getOperation().length());
        return minOperation.getFunction().apply(parseString(left), parseString(right));
    }

    private boolean isUnary(ExpressionString source, int offset) {
        return source.isGoodBracket(offset) || isVariable(source.substring(offset));
    }

    private boolean isVariable(ExpressionString c) {
        return c.toString().equals("x") || c.toString().equals("y") || c.toString().equals("z");
    }

    private BigInteger takeNumber(String number) {
        int i = 0;
        while (number.charAt(i) == '-') {
            ++i;
        }
        BigInteger result = new BigInteger(number.substring(i));
        if (i % 2 == 1)
            result = result.multiply(BigInteger.valueOf(-1));
        return result;
    }

    private boolean checkIsNumber(ExpressionString expression) {
        int i = 0;
        while (i < expression.length() && expression.charAt(i) == '-') {
            ++i;
        }
        while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
            ++i;
        }
        return i == expression.length() && !expression.isEmpty();
    }
}
