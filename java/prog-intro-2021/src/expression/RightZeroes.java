package expression;


public class RightZeroes extends AbstractUnary {
    public RightZeroes(EveryExpression expression) {
        super(expression, "t0",  operand -> {
            for (int i = 0; i <= 31; i++) {
                if ((operand & (1 << i)) != 0) {
                    return i;
                }
            }
            return 32;
        }, null);
    }
}
