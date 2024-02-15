package ru.ainur.lexer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ainur.parser.LexicalAnalyzer;
import ru.ainur.parser.Token;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestLexerManyTokens {
    private final Random random = new Random();

    @Test
    public void test2Tokens() throws ParseException {
        test("52 -", List.of(Token.N, Token.MINUS));
        test("+ 123", List.of(Token.PLUS, Token.N));


        test("cos sin\r", List.of(Token.FUNC, Token.FUNC));
        test("somefunc \t erf", List.of(Token.FUNC, Token.FUNC));

        test("+\t-", List.of(Token.PLUS, Token.MINUS));
        test(" * \t\r)", List.of(Token.MULTIPLY, Token.RPAREN));
        test("\t( 735\t", List.of(Token.LPAREN, Token.N));
    }

    @Test
    public void testInvalid() {
        assertThrows("12 _");
        assertThrows("sdf #");
        assertThrows("100_000");
        assertThrows("sin{10}");
        assertThrows("\\sin{10}");
        assertThrows("1/2");
    }

    private void assertThrows(String s) {
        Assertions.assertThrows(ParseException.class, () -> {
            LexicalAnalyzer lexer = new LexicalAnalyzer(new ByteArrayInputStream(s.getBytes()));
            lexer.nextToken();
            while (lexer.curToken() != Token.END) {
                lexer.nextToken();
            }
        });
    }


    @Test
    public void testBigger() throws ParseException {
        test("1 + 2 * 3", List.of(Token.N, Token.PLUS, Token.N, Token.MULTIPLY, Token.N));
        test("sin30 \r- cos\t 60", List.of(Token.FUNC, Token.N, Token.MINUS, Token.FUNC, Token.N));
    }


    @Test
    public void test100SmallStrings() {
        testManyTokens(100, 10);
    }

    @Test
    public void test10BigStrings() {
        testManyTokens(10, 100);
    }

    private void test(String s, List<Token> answer) throws ParseException {
        LexicalAnalyzer lexer = new LexicalAnalyzer(new ByteArrayInputStream(s.getBytes()));
        for (Token token : answer) {
            lexer.nextToken();
            Assertions.assertEquals(token, lexer.curToken());
        }

        lexer.nextToken();
        Assertions.assertEquals(Token.END, lexer.curToken());

        lexer.nextToken();
        Assertions.assertEquals(Token.END, lexer.curToken());
    }

    private void testManyTokens(int count, int length) {
        for (int i = 0; i < count; i++) {
            tryAnalyze(length, true);
            tryAnalyze(length, false);
        }
    }

    private void tryAnalyze(int length, boolean ws) {
        try {
            RandomTest sample = createTokenString(length, ws);
            test(sample.test, sample.tokens);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }


    private RandomTest createTokenString(int bound, boolean ws) {
        int length = random.nextInt(1, bound);

        StringBuilder sb = new StringBuilder();
        List<Token> tokens = new ArrayList<>();
        generateTokens(sb, tokens, length, ws);
        return new RandomTest(sb.toString(), tokens);
    }

    private void generateTokens(StringBuilder sb, List<Token> tokens, int length, boolean ws) {

        while (sb.length() < length) {
            switch (random.nextInt(7)) {
                case 0 -> {
                    tokens.add(Token.N);
                    int num = random.nextInt(1_000_000);
                    sb.append(String.valueOf(num));
                    sb.append(' ');
                }
                case 1 -> {

                    tokens.add(Token.FUNC);
                    sb.append(' ');
                    sb.append(IntStream.range(0, random.nextInt(1, 10))
                            .mapToObj(i -> (char) ('a' + random.nextInt(26)))
                            .map(Objects::toString)
                            .collect(Collectors.joining("")));
                    sb.append(' ');
                }
                case 2 -> {

                    tokens.add(Token.LPAREN);
                    sb.append('(');
                }
                case 3 -> {
                    tokens.add(Token.RPAREN);
                    sb.append(')');
                }
                case 4 -> {
                    tokens.add(Token.PLUS);
                    sb.append('+');
                }
                case 5 -> {
                    tokens.add(Token.MINUS);
                    sb.append(" - ");
                }
                case 6 -> {
                    tokens.add(Token.MULTIPLY);
                    sb.append('*');
                }
                default -> throw new RuntimeException("bad random");
            }
            if (ws && random.nextBoolean()) {
                sb.append(" ".repeat(random.nextInt(2)));
                sb.append("\t".repeat(random.nextInt(2)));
//                sb.append("\r".repeat(random.nextInt(2)));
            }
        }
    }

    private record RandomTest(String test, List<Token> tokens) {
    }
}
