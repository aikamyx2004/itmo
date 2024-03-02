package ru.ainur.generator;

import org.antlr.v4.runtime.tree.TerminalNode;
import ru.ainur.grammar.GrammarBaseListener;
import ru.ainur.grammar.GrammarParser;
import ru.ainur.parser.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfoListener extends GrammarBaseListener {
    private final GrammarInfo info = new GrammarInfo();

    @Override
    public void exitGrammarName(GrammarParser.GrammarNameContext ctx) {
        String grammarName = ctx.CAPITAL_START_IDENTIFIER().getText();
        info.setGrammarName(grammarName);
    }

    @Override
    public void exitPackageName(GrammarParser.PackageNameContext ctx) {
        info.setPackageName(ctx.getText());
        System.out.println("as");
    }

    @Override
    public void exitTerm(GrammarParser.TermContext ctx) {
        String name = ctx.CAPITAL_START_IDENTIFIER().getText();
        String regex = removeFirstLast(ctx.REGEX());
        info.getTerminals().add(new Terminal(name, regex));
//        info.getNameToType().put(name, RuleType.TERMINAL);
    }

    @Override
    public void exitNonTerm(GrammarParser.NonTermContext ctx) {
        String name = ctx.LOWER_START_IDENTIFIER().getText();
        List<Pair<String, String>> inherited = Collections.emptyList();
        List<Pair<String, String>> synthesized = Collections.emptyList();
        if (ctx.inh() != null) {
            inherited = getAttributes(ctx.inh().attributes());
        }
        if (ctx.synt() != null) {
            synthesized = getAttributes(ctx.synt().attributes());
        }

        List<NonTermRules> rules = new ArrayList<>();
        var nonTermRules = ctx.nonTermRules();
        for (var rule : nonTermRules.nonTermRule()) {
            List<Token> children = new ArrayList<>();
            if (rule.children != null)
                for (var child : rule.token()) {
                    String token = child.getChild(0).getText();
                    String code = removeFirstLast(child.INH_CODE());
                    children.add(new Token(token, code));
                }
            String syntCode = removeFirstLast(rule.BLOCKED_CODE());
            rules.add(new NonTermRules(children, syntCode));
        }
        var nonTerminals = info.getNonTerminals();
//        nonTerminals.putIfAbsent(name, new ArrayList<>());

        nonTerminals.add(new NonTerminal(name, inherited, synthesized, rules));
//        info.getNameToType().put(name, RuleType.NON_TERMINAL);
    }

    @Override
    public void exitHeader(GrammarParser.HeaderContext ctx) {
        if(ctx != null && ctx.BLOCKED_CODE()!= null){
            info.setHeader(removeFirstLast(ctx.BLOCKED_CODE()));
        }
    }

    private String removeFirstLast(TerminalNode blockedCode) {
        if (blockedCode == null) {
            return null;
        }
        String code = blockedCode.getText();
        return code.substring(1, code.length() - 1);
    }

    private List<Pair<String, String>> getAttributes(GrammarParser.AttributesContext attributes) {
        return attributes.attribute().stream()
                .map(a -> new Pair<>(a.attributeType.getText(), a.LOWER_START_IDENTIFIER().getText()))
                .toList();
    }

    public GrammarInfo getInfo() {
        return info;
    }
}
