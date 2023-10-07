package tests;

import expression.EveryExpression;
import expression.Expression;
import expression.parser.ExpressionParser;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {

        ExpressionParser w = new ExpressionParser();
        EveryExpression a = w.parse("-(0)");
        System.out.println(a.toMiniString());
        //(-804403710 min 1082363805) / - 1349584972 + t0 -306267733 * 1747857828 max - -338180711
//        (((-804403710 min 1082363805) / -(1349584972)) + ((t0(-306267733) * 1747857828) max -(-338180711)))
    }
}
