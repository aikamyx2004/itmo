package ru.ainur.lab1;


import ru.ainur.generator.tree.BaseNonTerminal;
import ru.ainur.generator.tree.TreeToken;

import java.text.ParseException;

import static ru.ainur.lab1.Lab1TreeClasses.*;

public class Lab1Parser {
    private final Lab1Lexer lexer;

    public Lab1Parser(Lab1Lexer lexer) {
        this.lexer = lexer;
    }

    public StartRuleContext startRule(StartRuleInherited startRuleInherited) throws ParseException {
        StartRuleContext startRuleContext = new StartRuleContext();
        switch (lexer.getCurrentToken()) {
            case NUMBER, FUNC, LPAREN, MINUS -> {
                var _in0 = new EInherited();
                var _child0 = e(_in0);
                startRuleContext.addChildren(_child0);

                var _child1 = expect(Lab1Token.EOF, startRuleContext);

            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return startRuleContext;
    }

    public EContext e(EInherited eInherited) throws ParseException {
        EContext eContext = new EContext();
        switch (lexer.getCurrentToken()) {
            case NUMBER, FUNC, LPAREN, MINUS -> {
                var _in0 = new TInherited();
                var _child0 = t(_in0);
                eContext.addChildren(_child0);

                var _in1 = new EPrimeInherited();
                var _child1 = ePrime(_in1);
                eContext.addChildren(_child1);

            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return eContext;
    }

    public EPrimeContext ePrime(EPrimeInherited ePrimeInherited) throws ParseException {
        EPrimeContext ePrimeContext = new EPrimeContext();
        switch (lexer.getCurrentToken()) {
            case PLUS -> {
                var _child0 = expect(Lab1Token.PLUS, ePrimeContext);

                var _in1 = new TInherited();
                var _child1 = t(_in1);
                ePrimeContext.addChildren(_child1);

                var _in2 = new EPrimeInherited();
                var _child2 = ePrime(_in2);
                ePrimeContext.addChildren(_child2);

            }
            case MINUS -> {
                var _child0 = expect(Lab1Token.MINUS, ePrimeContext);

                var _in1 = new TInherited();
                var _child1 = t(_in1);
                ePrimeContext.addChildren(_child1);

                var _in2 = new EPrimeInherited();
                var _child2 = ePrime(_in2);
                ePrimeContext.addChildren(_child2);

            }
            case RPAREN, EOF -> {
            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return ePrimeContext;
    }

    public TContext t(TInherited tInherited) throws ParseException {
        TContext tContext = new TContext();
        switch (lexer.getCurrentToken()) {
            case NUMBER, FUNC, LPAREN, MINUS -> {
                var _in0 = new FInherited();
                var _child0 = f(_in0);
                tContext.addChildren(_child0);

                var _in1 = new TPrimeInherited();
                var _child1 = tPrime(_in1);
                tContext.addChildren(_child1);

            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return tContext;
    }

    public TPrimeContext tPrime(TPrimeInherited tPrimeInherited) throws ParseException {
        TPrimeContext tPrimeContext = new TPrimeContext();
        switch (lexer.getCurrentToken()) {
            case MULTIPLY -> {
                var _child0 = expect(Lab1Token.MULTIPLY, tPrimeContext);

                var _in1 = new FInherited();
                var _child1 = f(_in1);
                tPrimeContext.addChildren(_child1);

                var _in2 = new TPrimeInherited();
                var _child2 = tPrime(_in2);
                tPrimeContext.addChildren(_child2);

            }
            case DIVIDE -> {
                var _child0 = expect(Lab1Token.DIVIDE, tPrimeContext);

                var _in1 = new FInherited();
                var _child1 = f(_in1);
                tPrimeContext.addChildren(_child1);

                var _in2 = new TPrimeInherited();
                var _child2 = tPrime(_in2);
                tPrimeContext.addChildren(_child2);

            }
            case RPAREN, EOF, PLUS, MINUS -> {
            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return tPrimeContext;
    }

    public FContext f(FInherited fInherited) throws ParseException {
        FContext fContext = new FContext();
        switch (lexer.getCurrentToken()) {
            case LPAREN -> {
                var _child0 = expect(Lab1Token.LPAREN, fContext);

                var _in1 = new EInherited();
                var _child1 = e(_in1);
                fContext.addChildren(_child1);

                var _child2 = expect(Lab1Token.RPAREN, fContext);

            }
            case MINUS -> {
                var _child0 = expect(Lab1Token.MINUS, fContext);

                var _in1 = new FInherited();
                var _child1 = f(_in1);
                fContext.addChildren(_child1);

            }
            case NUMBER -> {
                var _child0 = expect(Lab1Token.NUMBER, fContext);

            }
            case FUNC -> {
                var _child0 = expect(Lab1Token.FUNC, fContext);

                var _in1 = new FInherited();
                var _child1 = f(_in1);
                fContext.addChildren(_child1);

            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return fContext;
    }

    private TreeToken expect(Lab1Token token, BaseNonTerminal nt) throws ParseException {
        if (!token.equals(lexer.getCurrentToken())) {
            throw new ParseException(
                    "expected %s but found %s".formatted(token.name(), lexer.getCurrentToken().name()),
                    lexer.getPosition()
            );
        }
        var t = NAME_TO_CTOR.get(token).get();
        t.setText(lexer.getCurrentTokenString());
        nt.addChildren(t);
        lexer.nextToken();
        return t;
    }
}
