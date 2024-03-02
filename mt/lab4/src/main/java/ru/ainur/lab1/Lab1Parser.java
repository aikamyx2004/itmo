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
                var _child0 = e(null);
                startRuleContext.addChildren(_child0);
                var _child1 = expect(Lab1Token.EOF, startRuleContext);
                startRuleContext.res = _child0.res;
            }
            default -> throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return startRuleContext;
    }
    public EContext e(EInherited eInherited) throws ParseException {
        EContext eContext = new EContext();
        switch (lexer.getCurrentToken()) {
            case NUMBER, FUNC, LPAREN, MINUS -> {
                var _child0 = t(null);
                eContext.addChildren(_child0);
                var _child1 = ePrime(null);
                eContext.addChildren(_child1);
                eContext.res = _child0.res + _child1.res;
            }
            default -> throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return eContext;
    }
    public EPrimeContext ePrime(EPrimeInherited ePrimeInherited) throws ParseException {
        EPrimeContext ePrimeContext = new EPrimeContext();
        switch (lexer.getCurrentToken()) {
            case PLUS -> {
                var _child0 = expect(Lab1Token.PLUS, ePrimeContext);
                var _child1 = t(null);
                ePrimeContext.addChildren(_child1);
                var _child2 = ePrime(null);
                ePrimeContext.addChildren(_child2);
                ePrimeContext.res = _child1.res + _child2.res;
            }
            case MINUS -> {
                var _child0 = expect(Lab1Token.MINUS, ePrimeContext);
                var _child1 = t(null);
                ePrimeContext.addChildren(_child1);
                var _child2 = ePrime(null);
                ePrimeContext.addChildren(_child2);
                ePrimeContext.res = -_child1.res + _child2.res;
            }
            case RPAREN, EOF -> {
                ePrimeContext.res = 0;
            }
            default -> throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return ePrimeContext;
    }
    public TContext t(TInherited tInherited) throws ParseException {
        TContext tContext = new TContext();
        switch (lexer.getCurrentToken()) {
            case NUMBER, FUNC, LPAREN, MINUS -> {
                var _child0 = f(null);
                tContext.addChildren(_child0);
                var _child1 = tPrime(null);
                tContext.addChildren(_child1);
                tContext.res = _child0.res * _child1.res;
            }
            default -> throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return tContext;
    }
    public TPrimeContext tPrime(TPrimeInherited tPrimeInherited) throws ParseException {
        TPrimeContext tPrimeContext = new TPrimeContext();
        switch (lexer.getCurrentToken()) {
            case MULTIPLY -> {
                var _child0 = expect(Lab1Token.MULTIPLY, tPrimeContext);
                var _child1 = f(null);
                tPrimeContext.addChildren(_child1);
                var _child2 = tPrime(null);
                tPrimeContext.addChildren(_child2);
                tPrimeContext.res = _child1.res * _child2.res;
            }
            case DIVIDE -> {
                var _child0 = expect(Lab1Token.DIVIDE, tPrimeContext);
                var _child1 = f(null);
                tPrimeContext.addChildren(_child1);
                var _child2 = tPrime(null);
                tPrimeContext.addChildren(_child2);
                tPrimeContext.res = (1.0 / _child1.res) * _child2.res;
            }
            case RPAREN, EOF, PLUS, MINUS -> {
                tPrimeContext.res = 1;
            }
            default -> throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
        }
        return tPrimeContext;
    }
    public FContext f(FInherited fInherited) throws ParseException {
        FContext fContext = new FContext();
        switch (lexer.getCurrentToken()) {
            case LPAREN -> {
                var _child0 = expect(Lab1Token.LPAREN, fContext);
                var _child1 = e(null);
                fContext.addChildren(_child1);
                var _child2 = expect(Lab1Token.RPAREN, fContext);
                fContext.res = _child1.res;
            }
            case MINUS -> {
                var _child0 = expect(Lab1Token.MINUS, fContext);
                var _child1 = f(null);
                fContext.addChildren(_child1);
                fContext.res = -_child1.res;
            }
            case NUMBER -> {
                var _child0 = expect(Lab1Token.NUMBER, fContext);
                fContext.res = Integer.parseInt(_child0.getText());
            }
            case FUNC -> {
                var _child0 = expect(Lab1Token.FUNC, fContext);
                var _child1 = f(null);
                fContext.addChildren(_child1);
                fContext.res = Math.sin(_child1.res);
            }
            default -> throw new ParseException("unexpected token: " + lexer.getCurrentTokenString(), lexer.getPosition());
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
