package ru.ainur.parser;

import java.text.ParseException;

public class ExpressionParser {
    public Tree parse(String input) throws ParseException {
        return new BaseParser(input).parse();
    }
}
