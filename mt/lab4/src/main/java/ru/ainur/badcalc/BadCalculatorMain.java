package ru.ainur.badcalc;

import ru.ainur.calculator.CalculatorLexer;
import ru.ainur.calculator.CalculatorParser;
import ru.ainur.calculator.CalculatorTreeClasses;

import java.text.ParseException;
import java.util.Scanner;

public class BadCalculatorMain {
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

    private static BadCalculatorTreeClasses.StartRuleContext parse(String input) throws ParseException {
        BadCalculatorLexer lexer = new BadCalculatorLexer(input);
        BadCalculatorParser parser = new BadCalculatorParser(lexer);
        return parser.startRule(new BadCalculatorTreeClasses.StartRuleInherited());
    }
}
