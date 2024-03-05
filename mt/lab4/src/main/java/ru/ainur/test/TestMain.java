package ru.ainur.test;


import java.text.ParseException;

public class TestMain {
    public static void main(String[] args) throws ParseException {
        writeResult("2**2**2");
        writeResult("sin(tan(1/2))");
//        expect("", 0)
    }

    private static void expect(String s, int res) throws ParseException {
        var tree = parse(s);
//        if()
    }

    private static void writeResult(String s) throws ParseException {
        var tree = parse(s);
        System.out.printf("%s: %f%n", s, tree.res);
    }

    private static TestTreeClasses.EContext parse(String input) throws ParseException {
        TestLexer lexer = new TestLexer(input);
        TestParser parser = new TestParser(lexer);
        return parser.e(null);
    }
}