
package ru.ainur.test;

import ru.ainur.test.TestTreeClasses.*;

import java.util.Map;
import java.util.Set;

public class TestParser {
    private static final Map<String, Set<String>> FIRST = Map.ofEntries(
        Map.entry("NUMBER", Set.of("NUMBER")),
        Map.entry("FUNC", Set.of("FUNC")),
        Map.entry("e", Set.of("NUMBER", "FUNC", "LPAREN", "MINUS")),
        Map.entry("tPrime", Set.of("epsilon", "MULTIPLY", "DIVIDE")),
        Map.entry("f", Set.of("NUMBER", "FUNC", "LPAREN", "MINUS")),
        Map.entry("LPAREN", Set.of("LPAREN")),
        Map.entry("startRule", Set.of("NUMBER", "FUNC", "LPAREN", "MINUS")),
        Map.entry("RPAREN", Set.of("RPAREN")),
        Map.entry("ePrime", Set.of("epsilon", "PLUS", "MINUS")),
        Map.entry("MINUS", Set.of("MINUS")),
        Map.entry("DIVIDE", Set.of("DIVIDE")),
        Map.entry("t", Set.of("NUMBER", "FUNC", "LPAREN", "MINUS")),
        Map.entry("MULTIPLY", Set.of("MULTIPLY")),
        Map.entry("EOF", Set.of("EOF")),
        Map.entry("PLUS", Set.of("PLUS"))
    );


    private static final Map<String, Set<String>> FOLLOW = Map.ofEntries(
        Map.entry("NUMBER", Set.of("NUMBER")),
        Map.entry("FUNC", Set.of("FUNC")),
        Map.entry("e", Set.of("NUMBER", "FUNC", "LPAREN", "MINUS")),
        Map.entry("tPrime", Set.of("epsilon", "MULTIPLY", "DIVIDE")),
        Map.entry("f", Set.of("NUMBER", "FUNC", "LPAREN", "MINUS")),
        Map.entry("LPAREN", Set.of("LPAREN")),
        Map.entry("startRule", Set.of("NUMBER", "FUNC", "LPAREN", "MINUS")),
        Map.entry("RPAREN", Set.of("RPAREN")),
        Map.entry("ePrime", Set.of("epsilon", "PLUS", "MINUS")),
        Map.entry("MINUS", Set.of("MINUS")),
        Map.entry("DIVIDE", Set.of("DIVIDE")),
        Map.entry("t", Set.of("NUMBER", "FUNC", "LPAREN", "MINUS")),
        Map.entry("MULTIPLY", Set.of("MULTIPLY")),
        Map.entry("EOF", Set.of("EOF")),
        Map.entry("PLUS", Set.of("PLUS"))
    );


    private final TestLexer lexer;
    public TestParser(TestLexer lexer) {
        this.lexer = lexer;
    }
//    public TContext t(TInherited tInherited) {
//    }
//    public EContext e(EInherited eInherited) {
//    }
//    public TPrimeContext tPrime(TPrimeInherited tPrimeInherited) {
//    }
//    public FContext f(FInherited fInherited) {
//    }
//    public StartRuleContext startRule(StartRuleInherited startRuleInherited) {
//    }
//    public EPrimeContext ePrime(EPrimeInherited ePrimeInherited) {
//    }
}
