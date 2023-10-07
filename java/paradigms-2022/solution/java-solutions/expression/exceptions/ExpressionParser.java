package expression.exceptions;

import expression.*;

public class ExpressionParser implements TripleParser {
    @Override
    public EveryExpression parse(String expression) {
        return parse(new StringSource(expression));
    }

    public EveryExpression parse(CharSource source) {
        return new Parser(source).parse();
    }

    private static class Parser extends BaseParser {
        public Parser(CharSource source) {
            super(source);
        }

        public EveryExpression parse() {
            EveryExpression res = parseMinMax();
            if (!eof()) {
                throw new IllegalArgumentException("string parsing did not finished");
            }
            return res;
        }

        private EveryExpression parseMinMax() {
            EveryExpression minMaxTerms = parseTerm();
            while (true) {
                skipWhitespace();
                if (take("min")) {
                    if (between('0', '9')) {
                        throw error("near min need spaces");
                    }
                    skipWhitespace();
                    minMaxTerms = new Min(minMaxTerms, parseTerm());
                } else if (take("max")) {
                    if (between('0', '9')) {
                        throw error("near max need spaces");
                    }
                    skipWhitespace();
                    minMaxTerms = new Max(minMaxTerms, parseTerm());
                } else {
                    return minMaxTerms;
                }
            }
        }

        private EveryExpression parseTerm() {
            EveryExpression terms = parseFactor();
            while (true) {
                skipWhitespace();
                if (take('+')) {
                    terms = new CheckedAdd(terms, parseFactor());
                } else if (take('-')) {
                    terms = new CheckedSubtract(terms, parseFactor());
                } else {
                    return terms;
                }
            }
        }

        private EveryExpression parseFactor() {
            EveryExpression factor = parseExponent();
            while (true) {
                skipWhitespace();
                if (take('*')) {
                    factor = new CheckedMultiply(factor, parseExponent());
                } else if (take('/')) {
                    factor = new CheckedDivide(factor, parseExponent());
                } else {
                    return factor;
                }
            }
        }

        private EveryExpression parseExponent() {
            EveryExpression exponent = parseUnary();
            while (true) {
                skipWhitespace();
                if (take("**")) {
                    exponent = new Power(exponent, parseUnary());
                } else if (take("//")) {
                    exponent = new Log(exponent, parseUnary());
                } else {
                    return exponent;
                }
            }
        }

        private EveryExpression parseUnary() {
            skipWhitespace();
            if (take('a')) {
                expect("bs");
                if (!test(' ') && !test('(')) {
                    throw new IllegalArgumentException("after abs need space, found" + take());
                }
                return new Abs(parseUnary());
            } else if (take('-')) {
                if (between('0', '9')) {
                    back();
                    return new Const(takeNumber());
                }
                skipWhitespace();
                return new CheckedNegate(parseUnary());
            } else if (between('0', '9')) {
                return new Const(takeNumber());
            } else if (take('x')) {
                return new Variable("x");
            } else if (take('y')) {
                return new Variable("y");
            } else if (take('z')) {
                return new Variable("z");
            } else if (take('(')) {
                skipWhitespace();
                EveryExpression expr = parseMinMax();
                skipWhitespace();
                expect(')');
                return expr;
            }

            throw error("parsing did not finished, for input string");
        }

        private int takeNumber() {
            StringBuilder number = new StringBuilder();
            if (take('-'))
                number.append('-');
            while (between('0', '9')) {
                number.append(take());
            }
            return Integer.parseInt(number.toString());
        }
    }
}


// min max
// + -
// * /
// ** //


//abs
// -

/*
    E - expression

    E -> M min E | M max E | M   - min max expression
    M -> T  +  M | T  -  M | T   - term
    T -> F  *  T | F  /  T | F   - factor
    F -> P **  F | P //  F | P   - exponent function(pow, log)
    P ->   abs U |     - U | U   - unary expression
    U ->       C |       V | (E) - Const, Variable or expr in brackets
*/