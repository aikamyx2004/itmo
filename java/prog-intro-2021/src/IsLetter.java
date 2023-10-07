public class IsLetter implements CheckChar {
    @Override
    public boolean isGoodChar(char c) {
        return Character.isLetter(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION;
    }
}