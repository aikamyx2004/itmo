package ru.ainur.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class BaseParser {
    private final LexicalAnalyzer lexer;

    public BaseParser(InputStream inputStream) throws ParseException {
        this.lexer = new LexicalAnalyzer(inputStream);
        lexer.nextToken();
    }

    public BaseParser(String s) throws ParseException {
        this(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }

    public Tree parse() throws ParseException {
        Tree tree = E();
        expect(Token.END);
        return tree;
    }

    private Tree E() throws ParseException {
        Tree tree = new Tree("E");
        switch (lexer.curToken()) {
            case LPAREN:
            case MINUS:
            case N:
            case FUNC:
                tree.addChildren(T());
                tree.addChildren(EPrime());
                break;
            case END:
            case RPAREN:
                break;
            default:
                throw new ParseException("Unexpected token at E()", lexer.curPos());
        }
        return tree;
    }

    private Tree EPrime() throws ParseException {
        Tree tree = new Tree("E'");
        switch (lexer.curToken()) {
            case PLUS:
                expect(Token.PLUS);
                tree.addChildren(new Tree("+"));
                lexer.nextToken();

                tree.addChildren(T());
                tree.addChildren(EPrime());
                break;
            case MINUS:
                expect(Token.MINUS);
                tree.addChildren(new Tree("-"));
                lexer.nextToken();

                tree.addChildren(T());
                tree.addChildren(EPrime());
                break;
            case END:
            case RPAREN:
                break;

            default:
                throw new ParseException("Unexpected token at EPrime()", lexer.curPos());

        }
        return tree;
    }

    private Tree T() throws ParseException {
        Tree tree = new Tree("T");
        switch (lexer.curToken()) {
            case LPAREN:
            case MINUS:
            case N:
            case FUNC:
                tree.addChildren(F());
                tree.addChildren(TPrime());
                break;
            default:
                throw new ParseException("Unexpected token at T()", lexer.curPos());
        }

        return tree;
    }


    private Tree TPrime() throws ParseException {
        Tree tree = new Tree("T'");
        switch (lexer.curToken()) {
            case MULTIPLY:
                expect(Token.MULTIPLY);
                tree.addChildren(new Tree("*"));
                lexer.nextToken();
                tree.addChildren(F());
                tree.addChildren(TPrime());
                break;
            case END:
            case PLUS:
            case MINUS:
            case RPAREN:
                break;
            default:
                throw new ParseException("Unexpected token at TPrime()", lexer.curPos());
        }
        return tree;
    }

    private Tree F() throws ParseException {
        Tree tree = new Tree("F");
        switch (lexer.curToken()) {
            case LPAREN:
                //(
                expect(Token.LPAREN);
                tree.addChildren(new Tree("("));
                lexer.nextToken();
                //E
                tree.addChildren(E());
                //)
                expect(Token.RPAREN);
                tree.addChildren(new Tree(")"));
                lexer.nextToken();
                break;

            case N:
                //N
                expect(Token.N);
                tree.addChildren(new Tree("N"));
                lexer.nextToken();
                break;

            case FUNC:
                //FUNC
                expect(Token.FUNC);
                tree.addChildren(new Tree("FUNC"));
                lexer.nextToken();
                //F
                tree.addChildren(F());
                break;

            case MINUS:
                //-
                expect(Token.MINUS);
                tree.addChildren(new Tree("-"));
                lexer.nextToken();
                //F
                tree.addChildren(F());
                break;
            default:
                throw new ParseException("Unexpected token at F()", lexer.curPos());
        }
        return tree;
    }

    private void expect(Token token) throws ParseException {
        if (lexer.curToken() != token) {
            throw new ParseException("Expected " + token + " but found " + lexer.curToken(), lexer.curPos());
        }
    }
}
