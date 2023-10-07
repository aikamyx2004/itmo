package expression;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        EveryExpression a = new Multiply(new Variable("x"), new Variable("x"));
        EveryExpression b = new Multiply(new Const(2), new Variable("x"));
        EveryExpression ab = new Subtract(a, b);
        EveryExpression abc = new Add(ab, new Const(1));
        System.out.println(abc.evaluate(n));
    }
}
