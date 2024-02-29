package ru.ainur.generator;

import ru.ainur.parser.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LexerBase {
    private final List<Terminal> terminals;
    private final Pattern regex;

    public LexerBase(List<Terminal> terminals) {
        this.terminals = terminals;
        this.regex = Pattern.compile(
                terminals.stream()
                        .map(t -> "(?<%s>%s)".formatted(t.name(), t.regex()))
                        .collect(Collectors.joining("|"))
        );
    }

    public List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();

        Matcher matcher = regex.matcher(input);

        while (matcher.find()) {
            for (String tokenName : terminals.stream().map(Terminal::name).toList()) {
                String token = matcher.group(tokenName);
                if (token != null) {
                    tokens.add(tokenName);
                }
            }
        }

        return tokens;
    }

}
