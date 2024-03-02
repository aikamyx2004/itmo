package ru.ainur.generator;

import ru.ainur.generator.code.GeneratorUtil;
import ru.ainur.parser.NonTerminalRule;
import ru.ainur.parser.RuleType;
import ru.ainur.parser.Terminal;

import java.util.*;

public class GrammarInfo {
    private static final String EPSILON = "EPSILON";
    private static final String DOLLAR = "$";
    private List<Terminal> terminals = new ArrayList<>(List.of(new Terminal("EOF", "$")));
    private Map<String, List<NonTerminalRule>> nonTerminals = new HashMap<>();
    private Map<String, RuleType> nameToType = new HashMap<>();
    private String packageName;
    private String code;
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
            for (var nt : nonTerminals.entrySet()) {
                for (var rule : nt.getValue()) {
                    changes |= first.get(nt.getKey())
                            .addAll(computeFirstIteration(rule.nonTermRule()));
                }
            }
        } while (changes);
    }

    public Set<String> computeFirstIteration(List<String> alpha) {
        if (alpha.isEmpty()) {
            return new HashSet<>(Set.of(EPSILON));
        }
        if (GeneratorUtil.isTerminal(alpha.get(0))) {
            return new HashSet<>(Set.of(alpha.get(0)));
        }
        var firstA = first.get(alpha.get(0));
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
            for (var nt : nonTerminals.entrySet()) {
                for (var rule : nt.getValue()) {
                    var A = rule.name();
                    var alpha = rule.nonTermRule();
                    for (int i = 0; i < alpha.size(); i++) {
                        var B = alpha.get(i);
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



    public Set<String> countFirst1(NonTerminalRule rule) {
        var A = rule.name();
        var alpha = rule.nonTermRule();
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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


    private void initSet(Map<String, Set<String>> mp) {
        for (String nonTerminal : nonTerminals.keySet()) {
            mp.put(nonTerminal, new HashSet<>());
        }
    }
}
