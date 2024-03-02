
package ru.ainur.test;

import ru.ainur.generator.tree.BaseNonTerminal;
import ru.ainur.test.TestTreeClasses.*;

import java.text.ParseException;
import java.util.Map;
import java.util.Set;

import static ru.ainur.test.TestTreeClasses.NAME_TO_CTOR;
public class TestParser {
    private final TestLexer lexer;
    public TestParser(TestLexer lexer) {
        this.lexer = lexer;
    }
    public TContext t(TInherited tInherited) throws ParseException {
        TContext tContext = new TContext();
        switch (lexer.getCurrentToken()) {
            case NUMBER, FUNC, LPAREN, MINUS -> {
                tContext.addChildren(f(null));
                tContext.addChildren(tPrime(null));
            }
        }
        return tContext;
    }
    public EContext e(EInherited eInherited) throws ParseException {
        EContext eContext = new EContext();
        switch (lexer.getCurrentToken()) {
            case NUMBER, FUNC, LPAREN, MINUS -> {
                eContext.addChildren(t(null));
                eContext.addChildren(ePrime(null));
            }
        }
        return eContext;
    }
    public TPrimeContext tPrime(TPrimeInherited tPrimeInherited) throws ParseException {
        TPrimeContext tPrimeContext = new TPrimeContext();
        switch (lexer.getCurrentToken()) {
            case MULTIPLY -> {
                expect(TestToken.MULTIPLY, tPrimeContext);
                tPrimeContext.addChildren(f(null));
                tPrimeContext.addChildren(tPrime(null));
            }
            case DIVIDE -> {
                expect(TestToken.DIVIDE, tPrimeContext);
                tPrimeContext.addChildren(f(null));
                tPrimeContext.addChildren(tPrime(null));
            }
            case RPAREN, EOF, PLUS, MINUS -> {
            }
        }
        return tPrimeContext;
    }
    public FContext f(FInherited fInherited) throws ParseException {
        FContext fContext = new FContext();
        switch (lexer.getCurrentToken()) {
            case LPAREN -> {
                expect(TestToken.LPAREN, fContext);
                fContext.addChildren(e(null));
                expect(TestToken.RPAREN, fContext);
            }
            case MINUS -> {
                expect(TestToken.MINUS, fContext);
                fContext.addChildren(f(null));
            }
            case FUNC -> {
                expect(TestToken.FUNC, fContext);
                fContext.addChildren(f(null));
            }
            case NUMBER -> {
                expect(TestToken.NUMBER, fContext);
            }
        }
        return fContext;
    }
    public StartRuleContext startRule(StartRuleInherited startRuleInherited) throws ParseException {
        StartRuleContext startRuleContext = new StartRuleContext();
        switch (lexer.getCurrentToken()) {
            case NUMBER, FUNC, LPAREN, MINUS -> {
                startRuleContext.addChildren(e(null));
                expect(TestToken.EOF, startRuleContext);
            }
        }
        return startRuleContext;
    }
    public EPrimeContext ePrime(EPrimeInherited ePrimeInherited) throws ParseException {
        EPrimeContext ePrimeContext = new EPrimeContext();
        switch (lexer.getCurrentToken()) {
            case PLUS -> {
                expect(TestToken.PLUS, ePrimeContext);
                ePrimeContext.addChildren(t(null));
                ePrimeContext.addChildren(ePrime(null));
            }
            case MINUS -> {
                expect(TestToken.MINUS, ePrimeContext);
                ePrimeContext.addChildren(t(null));
                ePrimeContext.addChildren(ePrime(null));
            }
            case RPAREN, EOF -> {
            }
        }
        return ePrimeContext;
    }
    private void expect(TestToken token, BaseNonTerminal nt) throws ParseException {
        if (!token.equals(lexer.getCurrentToken())) {
            throw new ParseException(
                    "expected %s but found %s".formatted(token.name(), lexer.getCurrentToken().name()),
                    lexer.getPosition()
            );
        }
        nt.addChildren(NAME_TO_CTOR.get(token).get());
        lexer.nextToken();
    }
}
