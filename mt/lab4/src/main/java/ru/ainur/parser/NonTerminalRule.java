package ru.ainur.parser;

import java.util.List;

public record NonTerminalRule(String name,
                              List<Pair<String, String>> inh,
                              List<Pair<String, String>> synt,
                              List<String> nonTermRule,
                              String code) {
}
