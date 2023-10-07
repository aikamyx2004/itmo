package expression;


public class LeftZeroes extends AbstractUnary {
    public LeftZeroes(EveryExpression expression) {
        super(expression, "l0",  operand -> {
            for (int i = 31; i >= 0; i--) {
                if ((operand & (1 << i)) != 0) {
                    return 31 - i;
                }
            }
            return 32;
        }, null);
    }
}
