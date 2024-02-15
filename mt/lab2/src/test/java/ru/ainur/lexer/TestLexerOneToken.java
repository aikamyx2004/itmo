package ru.ainur.lexer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ainur.parser.LexicalAnalyzer;
import ru.ainur.parser.Token;

import java.io.ByteArrayInputStream;
import java.text.ParseException;

public class TestLexerOneToken {
    @Test
    public void testEnd() {
        expect(Token.END, "");
    }

    @Test
    public void testNumber() {
        expect(Token.N, "1");
        expect(Token.N, "-1");
        expect(Token.N, "52 (812)");
        expect(Token.N, "-812 + smth else");
    }

    @Test
    public void testFunction() {
        expect(Token.FUNC, "func");
        expect(Token.FUNC, "sin");
        expect(Token.FUNC, "tan * smth else");
        expect(Token.FUNC, "cos 34");
    }

    @Test
    public void testOtherTokens() {
        // PLUS, MINUS, MULTIPLY, LPAREN, RPAREN,
        testOneToken(Token.PLUS, "+");
        testOneToken(Token.MINUS, "-");
        testOneToken(Token.MULTIPLY, "*");
        testOneToken(Token.LPAREN, "(");
        testOneToken(Token.RPAREN, ")");
    }


    @Test
    public void testInvalid()  {
        assertThrows("_not_in_tokens");
        assertThrows("/not_in_tokens");
    }


    private void testOneToken(Token expected, String token) {
        expect(expected, token);
        expect(expected, token + " smth else");
        expect(expected, token + " ( 735");
        expect(expected, token + " +");
    }

    private void expect(Token expected, String input) {
        try {
            Token token = takeOneToken(input);
            Assertions.assertEquals(expected, token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Token takeOneToken(String input) throws ParseException {
        LexicalAnalyzer lexer = new LexicalAnalyzer(new ByteArrayInputStream(input.getBytes()));

        lexer.nextToken();
        return lexer.curToken();
    }

    private void assertThrows(String input) {
        Assertions.assertThrows(ParseException.class, () -> takeOneToken(input));
    }
}
