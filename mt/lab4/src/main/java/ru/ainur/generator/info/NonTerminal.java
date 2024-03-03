package ru.ainur.generator.info;

import java.util.List;

public record NonTerminal(String name,
                          List<Pair<String, String>> inh,
                          List<Pair<String, String>> synt,
                          List<NonTermRules> nonTermRule) {
}
