import expression.EveryExpression;
import expression.exceptions.ExpressionParser;
import expression.exceptions.OverflowException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Expression parser can handle with min(*, *), max(*, *), +, -, *, /, **, //");
            System.out.println("Where a ** b is a ^ b, a // b is log(a, b).");
            System.out.println("Operations are written in ascending order of priority");
            System.out.println("Write expression with x, y, z");
            String string = in.nextLine();

            EveryExpression expression = new ExpressionParser().parse(string);
            System.out.println("Expression parsed like that:");
            System.out.println(expression.toMiniString());

            System.out.println("Write x, y, and z");
            System.out.println("If variable is not used, write 0");
            int x = in.nextInt();
            int y = in.nextInt();
            int z = in.nextInt();
            try {
                System.out.println("Expression equals: " + expression.evaluate(x, y, z));
            } catch (OverflowException | ArithmeticException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
