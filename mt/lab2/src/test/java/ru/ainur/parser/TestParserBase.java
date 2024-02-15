package ru.ainur.parser;

import org.junit.jupiter.api.Assertions;

import java.text.ParseException;

public class TestParserBase {
    private final ExpressionParser parser = new ExpressionParser();

    protected void test(String s, Tree expected) throws ParseException {
        Assertions.assertEquals(expected, parse(s));
    }

    protected void testInvalid(String input) {
        Assertions.assertThrows(ParseException.class, () -> parse(input));
    }

    protected Tree parse(String s) throws ParseException {
        return parser.parse(s);
    }
}
