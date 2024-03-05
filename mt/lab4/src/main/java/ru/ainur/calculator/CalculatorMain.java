package ru.ainur.calculator;

import java.text.ParseException;
import java.util.Scanner;

public class CalculatorMain {
    public static void main(String[] args) throws ParseException {
        Scanner in = new Scanner(System.in);
        while (true) {
            String s;
            s = in.nextLine();
            try {
                var e = parse(s);
                System.out.println(e.res);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static CalculatorTreeClasses.StartRuleContext parse(String input) throws ParseException {
        CalculatorLexer lexer = new CalculatorLexer(input);
        CalculatorParser parser = new CalculatorParser(lexer);
        return parser.startRule(new CalculatorTreeClasses.StartRuleInherited());
    }
}
