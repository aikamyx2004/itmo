package parser;

import expression.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class Parser extends BaseParser {
    public Parser(String expr) {
        super(new StringSource(expr));
    }

    public Expression parse() {
        Expression expr = parseExpression();
        skipWhitespace();
        if (!eof()) {
            throw new IllegalArgumentException("expected end of source");
        }
        return expr;
    }

    private Expression parseExpression() {
        skipWhitespace();
        Expression disj = parseDisj();
        while (true) {
            if (eof()) {
                break;
            }
            skipWhitespace();
            if (!take("->")) {
                break;
            } // taken ->
            Expression expr = parseExpression();
            disj = new Implication(disj, expr);
        }
        return disj;
    }


    private Expression parseDisj() {
        skipWhitespace();
        List<Expression> conjs = new ArrayList<>();
        // taken |
        do {
            Expression expr = parseConj();
            conjs.add(expr);
            skipWhitespace();
        } while (take("|"));
        return union(conjs, Disjunction::new);
    }

    private Expression parseConj() {
        skipWhitespace();
        List<Expression> negs = new ArrayList<>();
        // taken |
        do {
            Expression expr = parseNeg();
            negs.add(expr);
            skipWhitespace();
        } while (take("&"));
        return union(negs, Conjunction::new);
//
//        Expression neg = parseNeg();
//        while (true) {
//            if (eof()) {
//                break;
//            }
//            if (!take("&")) {
//                break;
//            } // taken ->
//            Expression expr = parseNeg();
//            neg = new Conjunction(neg, expr);
//        }
//        return neg;
    }

    private Expression parseNeg() {
        skipWhitespace();
        if (take('!')) {
            return new Negation(parseNeg());
        }
        if (take('(')) {
            Expression res = parseExpression();
            expect(')');
            return res;
        }
        if (take('_')) {
            expect("|_");
            return new False();
        }
        if (between('A', 'Z')) {
            return parseVariable();
        }
        throw new IllegalArgumentException("smth bad happened");
    }

    private Variable parseVariable() {
        StringBuilder sb = new StringBuilder();
        while (between('A', 'Z') || between('0', '9') || test('\'')) {
            sb.append(take());
        }
        return new Variable(sb.toString());
    }

    private Expression union(List<Expression> parts, BiFunction<Expression, Expression, Expression> type) {
        Expression conj = parts.get(0);
        for (int i = 1; i < parts.size(); i++) {
            conj = type.apply(conj, parts.get(i));
        }
        return conj;
    }

    private void skipWhitespace() {
        while (take(Character::isWhitespace)) {
            //do nothing
        }
    }
}