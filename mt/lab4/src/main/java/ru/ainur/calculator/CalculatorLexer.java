package ru.ainur.calculator;


import ru.ainur.generator.info.Terminal;

import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CalculatorLexer {
    public static final List<Terminal> TERMINALS = List.of(
            new Terminal("EOF", "$"),
            new Terminal("PLUS", "\\+"),
            new Terminal("MINUS", "-"),
            new Terminal("NUMBER", "\\d+"),
            new Terminal("MULTIPLY", "\\*"),
            new Terminal("DIVIDE", "/"),
            new Terminal("FUNC", "\\w+"),
            new Terminal("LPAREN", "\\("),
            new Terminal("RPAREN", "\\)")
    );
    private final String input;
    private final Pattern regex;
    private final Matcher matcher;
    private CalculatorToken currentToken;
    private String currentTokenString;

    public CalculatorLexer(String input) throws ParseException {
        this.input = input;
        this.regex = Pattern.compile(
                TERMINALS.stream()
                        .map(t -> "(?<%s>%s)".formatted(t.name(), t.regex()))
                        .collect(Collectors.joining("|"))
        );
        matcher = regex.matcher(input);
        nextToken();
    }

    public void nextToken() throws ParseException {
        if (!matcher.find()) {
            if (currentToken != CalculatorToken.EOF) {
                throw new ParseException("it is not token", matcher.start());
            }
        }
        for (int i = 0; i < TERMINALS.size(); i++) {
            String match = matcher.group(TERMINALS.get(i).name());
            if (match != null) {
                currentToken = CalculatorToken.values()[i];
                currentTokenString = match;
            }
        }
    }

    public CalculatorToken getCurrentToken() {
        return currentToken;
    }

    public String getCurrentTokenString() {
        return currentTokenString;
    }

    public int getPosition() {
        return matcher.start();
    }

}
