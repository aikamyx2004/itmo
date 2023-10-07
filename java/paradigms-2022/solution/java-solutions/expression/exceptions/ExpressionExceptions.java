package expression.exceptions;

public class ExpressionExceptions {
    public static class OverflowException extends RuntimeException {
        public OverflowException(String errorMessage) {
            super(errorMessage);
        }
    }
}
