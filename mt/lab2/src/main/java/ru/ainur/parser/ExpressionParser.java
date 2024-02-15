package ru.ainur.parser;

import java.text.ParseException;

public class ExpressionParser {
    public Tree parse(String input) throws ParseException {
        BaseParser parser1 = new BaseParser(input);
        return parser1.parse();
    }
}
