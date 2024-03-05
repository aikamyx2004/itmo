package ru.ainur.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class TestCalculator {
    @Test
    public void testArithmetic() throws ParseException {
        expect("52", 52);
        expect("0", 0);
        expect("-812", -812);

        expect("1 + 1", 2);
        expect("1 - 2", -1);
        expect("2 * 2", 4);
        expect("1 / 2", 1.0 / 2);

        expect("(1)", 1);

        expect("(5 + 3) * 2", 16);
        expect("10 / (2 + 3) - 1", 1);
        expect(" 4 * (6 - 2)", 16);
        expect("3 + 7 - 1", 9);
        expect("((10 + 5) / 3 + 7) / 2 - 4", 2);
        expect("(20 - 5) * (8 + 2) / 3 + 15", 65);
//        expect("12", 12);
    }

    private void expect(String input, double number) throws ParseException {
        var t = parse(input);
        Assertions.assertEquals(number, t.res);
    }

    private static CalculatorTreeClasses.StartRuleContext parse(String input) throws ParseException {
        CalculatorLexer lexer = new CalculatorLexer(input);
        CalculatorParser parser = new CalculatorParser(lexer);
        return parser.startRule(new CalculatorTreeClasses.StartRuleInherited());
    }
}
