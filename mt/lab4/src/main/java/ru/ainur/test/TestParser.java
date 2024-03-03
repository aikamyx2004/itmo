package ru.ainur.test;


import ru.ainur.generator.tree.BaseNonTerminal;
import ru.ainur.generator.tree.TreeToken;

import java.text.ParseException;

import static ru.ainur.test.TestTreeClasses.*;

public class TestParser {
    private final TestLexer lexer;

    public TestParser(TestLexer lexer) {
        this.lexer = lexer;
    }

    public StartRuleContext startRule(StartRuleInherited startRuleInherited) throws ParseException {
        StartRuleContext startRuleContext = new StartRuleContext();
        switch (lexer.getCurrentToken()) {
            case TAN, NUMBER, COS, LPAREN, SIN, MINUS -> {
                var _in0 = new EInherited();
                var _child0 = e(_in0);
                startRuleContext.addChildren(_child0);

                var _child1 = expect(TestToken.EOF, startRuleContext);

                startRuleContext.res = _child0.res;
            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return startRuleContext;
    }

    public EContext e(EInherited eInherited) throws ParseException {
        EContext eContext = new EContext();
        switch (lexer.getCurrentToken()) {
            case TAN, NUMBER, COS, LPAREN, SIN, MINUS -> {
                var _in0 = new TInherited();
                var _child0 = t(_in0);
                eContext.addChildren(_child0);

                var _in1 = new EPrimeInherited();
                _in1.res = _child0.res;
                var _child1 = ePrime(_in1);
                eContext.addChildren(_child1);

                eContext.res = _child1.res;
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
                var _child0 = expect(TestToken.PLUS, ePrimeContext);

                var _in1 = new TInherited();
                var _child1 = t(_in1);
                ePrimeContext.addChildren(_child1);

                var _in2 = new EPrimeInherited();
                _in2.res = ePrimeInherited.res + _child1.res;
                var _child2 = ePrime(_in2);
                ePrimeContext.addChildren(_child2);

                ePrimeContext.res = _child2.res;
            }
            case MINUS -> {
                var _child0 = expect(TestToken.MINUS, ePrimeContext);

                var _in1 = new TInherited();
                var _child1 = t(_in1);
                ePrimeContext.addChildren(_child1);

                var _in2 = new EPrimeInherited();
                _in2.res = ePrimeInherited.res - _child1.res;
                var _child2 = ePrime(_in2);
                ePrimeContext.addChildren(_child2);

                ePrimeContext.res = _child2.res;
            }
            case RPAREN, EOF -> {
                ePrimeContext.res = ePrimeInherited.res;
            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return ePrimeContext;
    }

    public TContext t(TInherited tInherited) throws ParseException {
        TContext tContext = new TContext();
        switch (lexer.getCurrentToken()) {
            case TAN, NUMBER, COS, LPAREN, SIN, MINUS -> {
                var _in0 = new PInherited();
                var _child0 = p(_in0);
                tContext.addChildren(_child0);

                var _in1 = new TPrimeInherited();
                _in1.res = _child0.res;
                var _child1 = tPrime(_in1);
                tContext.addChildren(_child1);

                tContext.res = _child1.res;
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
                var _child0 = expect(TestToken.MULTIPLY, tPrimeContext);

                var _in1 = new PInherited();
                var _child1 = p(_in1);
                tPrimeContext.addChildren(_child1);

                var _in2 = new TPrimeInherited();
                _in2.res = tPrimeInherited.res * _child1.res;
                var _child2 = tPrime(_in2);
                tPrimeContext.addChildren(_child2);

                tPrimeContext.res = _child2.res;
            }
            case DIVIDE -> {
                var _child0 = expect(TestToken.DIVIDE, tPrimeContext);

                var _in1 = new PInherited();
                var _child1 = p(_in1);
                tPrimeContext.addChildren(_child1);

                var _in2 = new TPrimeInherited();
                _in2.res = tPrimeInherited.res / _child1.res;
                var _child2 = tPrime(_in2);
                tPrimeContext.addChildren(_child2);

                tPrimeContext.res = _child2.res;
            }
            case RPAREN, EOF, PLUS, MINUS -> {
                tPrimeContext.res = tPrimeInherited.res;
            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return tPrimeContext;
    }

    public PContext p(PInherited pInherited) throws ParseException {
        PContext pContext = new PContext();
        switch (lexer.getCurrentToken()) {
            case TAN, NUMBER, COS, LPAREN, SIN, MINUS -> {
                var _in0 = new FInherited();
                var _child0 = f(_in0);
                pContext.addChildren(_child0);

                var _in1 = new PPrimeInherited();
                _in1.res = _child0.res;
                var _child1 = pPrime(_in1);
                pContext.addChildren(_child1);

                pContext.res = _child1.res;
            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return pContext;
    }

    public PPrimeContext pPrime(PPrimeInherited pPrimeInherited) throws ParseException {
        PPrimeContext pPrimeContext = new PPrimeContext();
        switch (lexer.getCurrentToken()) {
            case POWER -> {
                var _child0 = expect(TestToken.POWER, pPrimeContext);

                var _in1 = new FInherited();
                var _child1 = f(_in1);
                pPrimeContext.addChildren(_child1);

                var _in2 = new PPrimeInherited();
                _in2.res = Math.pow(pPrimeInherited.res, _child1.res);
                var _child2 = pPrime(_in2);
                pPrimeContext.addChildren(_child2);

                pPrimeContext.res = _child2.res;
            }
            case MULTIPLY, RPAREN, EOF, DIVIDE, PLUS, MINUS -> {
                pPrimeContext.res = pPrimeInherited.res;
            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return pPrimeContext;
    }

    public FContext f(FInherited fInherited) throws ParseException {
        FContext fContext = new FContext();
        switch (lexer.getCurrentToken()) {
            case LPAREN -> {
                var _child0 = expect(TestToken.LPAREN, fContext);

                var _in1 = new EInherited();
                var _child1 = e(_in1);
                fContext.addChildren(_child1);

                var _child2 = expect(TestToken.RPAREN, fContext);

                fContext.res = _child1.res;
            }
            case MINUS -> {
                var _child0 = expect(TestToken.MINUS, fContext);

                var _in1 = new FInherited();
                var _child1 = f(_in1);
                fContext.addChildren(_child1);

                fContext.res = -_child1.res;
            }
            case SIN -> {
                var _child0 = expect(TestToken.SIN, fContext);

                var _in1 = new FInherited();
                var _child1 = f(_in1);
                fContext.addChildren(_child1);

                fContext.res = Math.sin(_child1.res);
            }
            case COS -> {
                var _child0 = expect(TestToken.COS, fContext);

                var _in1 = new FInherited();
                var _child1 = f(_in1);
                fContext.addChildren(_child1);

                fContext.res = Math.cos(_child1.res);
            }
            case TAN -> {
                var _child0 = expect(TestToken.TAN, fContext);

                var _in1 = new FInherited();
                var _child1 = f(_in1);
                fContext.addChildren(_child1);

                fContext.res = Math.tan(_child1.res);
            }
            case NUMBER -> {
                var _child0 = expect(TestToken.NUMBER, fContext);

                fContext.res = Integer.parseInt(_child0.getText());
            }
            default ->
                    throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return fContext;
    }

    private TreeToken expect(TestToken token, BaseNonTerminal nt) throws ParseException {
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
