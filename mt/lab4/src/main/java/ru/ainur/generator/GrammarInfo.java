package ru.ainur.generator;

import ru.ainur.generator.code.GeneratorUtil;
import ru.ainur.parser.NonTerminal;
import ru.ainur.parser.Terminal;
import ru.ainur.parser.Token;

import java.util.*;

public class GrammarInfo {
    private static final String EPSILON = "EPSILON";
    private static final String DOLLAR = "$";
    private List<Terminal> terminals = new ArrayList<>(List.of(new Terminal("EOF", "$")));
    private List<NonTerminal> nonTerminals = new ArrayList<>();
    private String packageName;
    private String header;
    private String grammarName;
    private final Map<String, Set<String>> first = new HashMap<>();
    private final Map<String, Set<String>> follow = new HashMap<>();

    public void countFirstFollow() {
        computeFirst();
        computeFollow();
    }

    private void computeFirst() {
        initSet(first);
        boolean changes;
        do {
            changes = false;
            for (var nt : nonTerminals) {
                for (var rule : nt.nonTermRule()) {
                    changes |= first.get(nt.name())
                            .addAll(computeFirstIteration(rule.tokens()));
                }
            }
        } while (changes);
    }

    public Set<String> computeFirstIteration(List<Token> alpha) {
        if (alpha.isEmpty()) {
            return new HashSet<>(Set.of(EPSILON));
        }
        if (GeneratorUtil.isTerminal(alpha.get(0).name())) {
            return new HashSet<>(Set.of(alpha.get(0).name()));
        }
        var firstA = first.get(alpha.get(0).name());
        var answer = new HashSet<>(firstA);
        answer.remove(EPSILON);

        if (firstA.contains(EPSILON)) {
            answer.addAll(computeFirstIteration(alpha.subList(1, alpha.size())));
        }
        return answer;
    }

    public void computeFollow() {
        initSet(follow);
        for (Terminal t : terminals) {
            follow.put(t.name(), new HashSet<>());
        }
        follow.get("startRule").add(DOLLAR);

        boolean changes;
        do {
            changes = false;
            for (var nt : nonTerminals) {
                for (var rule : nt.nonTermRule()) {
                    var A = nt.name();
                    var alpha = rule.tokens();
                    for (int i = 0; i < alpha.size(); i++) {
                        var B = alpha.get(i).name();
                        var gamma = alpha.subList(i + 1, alpha.size());
                        var firstGamma = computeFirstIteration(gamma);
                        boolean EPS_inside = firstGamma.remove(EPSILON);

                        changes |= follow.get(B).addAll(firstGamma);
                        if (EPS_inside) {
                            changes |= follow.get(B).addAll(follow.get(A));
                        }
                    }
                }
            }
        } while (changes);
    }



    public Set<String> countFirst1(List<Token> alpha, String A) {
        var firsts = computeFirstIteration(alpha);
        if(firsts.contains(EPSILON)){
            firsts.addAll(follow.get(A));
        }
        firsts.remove(EPSILON);
        return firsts;
    }

    public Map<String, Set<String>> getFirst() {
        return first;
    }

    public Map<String, Set<String>> getFollow() {
        return follow;
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

    public List<NonTerminal> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(List<NonTerminal> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
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


    private void initSet(Map<String, Set<String>> mp) {
        for (var nonTerminal : nonTerminals) {
            mp.put(nonTerminal.name(), new HashSet<>());
        }
    }
}
