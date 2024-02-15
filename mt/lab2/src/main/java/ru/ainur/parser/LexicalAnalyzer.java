package ru.ainur.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.function.Predicate;

public class LexicalAnalyzer {
    private final InputStream is;
    private int curChar;
    private int curPos;
    private Token curToken;

    public LexicalAnalyzer(InputStream is) throws ParseException {
        this.is = is;
        curPos = 0;
        nextChar();
    }

    private boolean isBlank(int c) {
        return c == ' ' || c == '\r' || c == '\n' || c == '\t';
    }

    private void nextChar() throws ParseException {
        curPos++;
        try {
            curChar = is.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), curPos);
        }
    }

    public void nextToken() throws ParseException {
        skipWs();
        if (checkSequence(Character::isDigit, Token.N)) {
            return;
        }
        if (checkSequence(Character::isLetter, Token.FUNC)) {
            return;
        }

        switch (curChar) {
            case '(' -> {
                nextChar();
                curToken = Token.LPAREN;
            }

            case ')' -> {
                nextChar();
                curToken = Token.RPAREN;
            }
            case '+' -> {
                nextChar();
                curToken = Token.PLUS;
            }
            case '-' -> {
                nextChar();
                if (checkSequence(Character::isDigit, Token.N)) {
                    return;
                }
                curToken = Token.MINUS;
            }
            case '*' -> {
                nextChar();
                curToken = Token.MULTIPLY;
            }
            case -1 -> curToken = Token.END;
            default -> throw new ParseException("Illegal character: " + (char) curChar, curPos);
        }
    }

    private void skipWs() throws ParseException {
        while (isBlank(curChar)) {
            nextChar();
        }
    }

    private boolean checkSequence(Predicate<Integer> predicate, Token token) throws ParseException {
        if (predicate.test(curChar)) {
            curToken = token;
            while (predicate.test(curChar)) {
                nextChar();
            }
            return true;
        }
        return false;
    }

    public Token curToken() {
        return curToken;
    }

    public int curPos() {
        return curPos;
    }
}

