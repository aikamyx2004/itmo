package ru.ainur.generator;

import ru.ainur.parser.NonTerminalRule;
import ru.ainur.parser.RuleType;
import ru.ainur.parser.Terminal;

import java.util.*;

public class GrammarInfo {
    private List<Terminal> terminals = new ArrayList<>(List.of(new Terminal("EOF", "$")));
    private Map<String, List<NonTerminalRule>> nonTerminals = new HashMap<>();
    private Map<String, RuleType> nameToType = new HashMap<>();
    private String header;
    private String code;
    private String grammarName;
    private final Map<String, Set<String>> first = new HashMap<>();
    private final Map<String, Set<String>> follow = new HashMap<>();

    public void countFirstFollow() {
        computeFirst();
        computeFollow();
    }
    private void computeFirst() {
        for (Terminal terminal : terminals) {
            first.put(terminal.name(), new HashSet<>(List.of(terminal.name())));
        }

        for (String nonTerminal : nonTerminals.keySet()) {
            first.put(nonTerminal, new HashSet<>());
        }

        boolean changes;
        do {
            changes = computeFirstIteration();
        } while (changes);
    }

    private boolean computeFirstIteration() {
        boolean changes = false;
        for (String nonTerminal : nonTerminals.keySet()) {
            for (NonTerminalRule rule : nonTerminals.get(nonTerminal)) {
                List<String> symbols = rule.nonTermRule();
                int i = 0;
                while (i < symbols.size()) {
                    String symbol = symbols.get(i);
                    if (terminals.stream().anyMatch(t -> t.name().equals(symbol))) {
                        changes |= first.get(nonTerminal).add(symbol);
                        break;
                    } else if (symbol.equals("epsilon")) {
                        changes |= first.get(nonTerminal).add(symbol);
                        break;
                    } else {
                        Set<String> firstOfSymbol = first.getOrDefault(symbol, Collections.emptySet());
                        changes |= first.get(nonTerminal).addAll(firstOfSymbol);
                        if (!firstOfSymbol.contains("epsilon")) {
                            break;
                        }
                    }
                    i++;
                }
                if (i == symbols.size()) {
                    changes |= first.get(nonTerminal).add("epsilon");
                }
            }
        }
        return changes;
    }

    private void computeFollow() {
        for (String nonTerminal : nonTerminals.keySet()) {
            follow.put(nonTerminal, new HashSet<>());
        }

        follow.get("startRule").add("EOF");

        boolean changes;
        do {
            changes = computeFollowIteration();
        } while (changes);
    }

    private boolean computeFollowIteration() {
        boolean changes = false;
        for (String nonTerminal : nonTerminals.keySet()) {
            for (NonTerminalRule rule : nonTerminals.get(nonTerminal)) {
                List<String> symbols = rule.nonTermRule();
                for (int i = 0; i < symbols.size() - 1; i++) {
                    String symbol = symbols.get(i);
                    if (nonTerminals.containsKey(symbol)) {
                        List<String> beta = symbols.subList(i + 1, symbols.size());
                        Set<String> firstOfBeta = firstOf(beta);
                        if (!firstOfBeta.contains("epsilon")) {
                            changes |= follow.get(symbol).addAll(firstOfBeta);
                        } else {
                            changes |= follow.get(symbol).addAll(firstOfBeta);
                            changes |= follow.get(symbol).addAll(follow.get(nonTerminal));
                        }
                    }
                }
                String lastSymbol = symbols.get(symbols.size() - 1);
                if (nonTerminals.containsKey(lastSymbol)) {
                    changes |= follow.get(lastSymbol).addAll(follow.get(nonTerminal));
                }
            }
        }
        return changes;
    }

    private Set<String> firstOf(List<String> symbols) {
        Set<String> result = new HashSet<>();
        int i = 0;
        while (i < symbols.size()) {
            String symbol = symbols.get(i);
            if (terminals.stream().anyMatch(t -> t.name().equals(symbol))) {
                result.add(symbol);
                break;
            } else {
                result.addAll(first.get(symbol));
                if (!first.get(symbol).contains("epsilon")) {
                    break;
                }
            }
            i++;
        }
        if (i == symbols.size()) {
            result.add("epsilon");
        }
        return result;
    }

    public Map<String, Set<String>> getFirst() {
        return first;
    }

    public Map<String, Set<String>> getFollow() {
        return follow;
    }

    public Map<String, RuleType> getNameToType() {
        return nameToType;
    }

    public void setNameToType(Map<String, RuleType> nameToType) {
        this.nameToType = nameToType;
    }

    public String getGrammarName() {
        return grammarName;
    }

    public void setGrammarName(String grammarName) {
        this.grammarName = grammarName;
    }

    public List<Terminal> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<Terminal> terminals) {
        this.terminals = terminals;
    }

    public Map<String, List<NonTerminalRule>> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(Map<String, List<NonTerminalRule>> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTokenClassName() {
        return "%sToken".formatted(grammarName);
    }

    public String getLexerClassName() {
        return "%sLexer".formatted(grammarName);
    }

    public String getParserClassName() {
        return "%sParser".formatted(grammarName);
    }

    public String getTreeClassesClassName() {
        return "%sTreeClasses".formatted(grammarName);
    }
}
