class IsNumber implements CheckChar {
    @Override
    public boolean isGoodChar(char c) {
        c = Character.toLowerCase(c);
        return Character.isDigit(c) || ('a' <= c && c <= 'f') || c == 'x' || c == '-';
    }
}