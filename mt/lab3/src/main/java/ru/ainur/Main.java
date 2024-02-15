package ru.ainur;


import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import ru.ainur.parser.ExpressionLexer;
import ru.ainur.parser.ExpressionParser;

public class Main {
    public static void main(String[] args) {
        ExpressionLexer lexer = new ExpressionLexer(CharStreams.fromString("4*4"));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);
        ExpressionParser.ExpressionContext parseTree = parser.expression();
        System.out.println(parseTree.toStringTree());

    }
}
