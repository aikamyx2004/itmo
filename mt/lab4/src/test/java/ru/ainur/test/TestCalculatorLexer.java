package ru.ainur.test;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class TestCalculatorLexer {
    @Test
    public void testLexer() throws ParseException {
        CalculatorLexer lexer = new CalculatorLexer("1 + 2 * / -");
        System.out.println(lexer.getCurrentToken());
        lexer.nextToken();
        System.out.println(lexer.getCurrentToken());
        lexer.nextToken();
        System.out.println(lexer.getCurrentToken());
        lexer.nextToken();
        System.out.println(lexer.getCurrentToken());
        lexer.nextToken();
        System.out.println(lexer.getCurrentToken());
        lexer.nextToken();
        System.out.println(lexer.getCurrentToken());
        lexer.nextToken();
        System.out.println(lexer.getCurrentToken());
        lexer.nextToken();
        System.out.println(lexer.getCurrentToken());
        lexer.nextToken();
        System.out.println(lexer.getCurrentToken());
        lexer.nextToken();
        System.out.println(lexer.getCurrentToken());
        lexer.nextToken();
        System.out.println(lexer.getCurrentToken());
    }

    @Test
    public void testEmpty() throws ParseException {
        CalculatorLexer lexer = new CalculatorLexer("");
        lexer.nextToken();
        lexer.nextToken();
        lexer.nextToken();
        System.out.println(lexer.getCurrentTokenString());
        System.out.println(lexer.getCurrentTokenString());
    }
}
