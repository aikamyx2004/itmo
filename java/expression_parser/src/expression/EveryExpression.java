package expression;

public interface EveryExpression extends TripleExpression, Expression {
    default boolean checkFirstBrackets() {
        return false;
    }

    default boolean checkSecondBrackets() {
        return false;
    }

    default void fillMiniString(final StringBuilder result) {
        result.append(this);
    }

    default void fillString(final StringBuilder result) {
        result.append(this);
    }

    @Override
    default String toMiniString() {
        StringBuilder result = new StringBuilder();
        fillMiniString(result);
        return result.toString();
    }

    default void putBrackets(final StringBuilder result, final boolean needBrackets) {
        if (needBrackets) {
            result.append('(');
        }
        fillMiniString(result);
        if (needBrackets) {
            result.append(')');
        }
    }
}
