package ru.ainur.generator;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.ainur.grammar.GrammarBaseListener;
import ru.ainur.grammar.GrammarParser;
import ru.ainur.parser.NonTerminalRule;
import ru.ainur.parser.Pair;
import ru.ainur.parser.RuleType;
import ru.ainur.parser.Terminal;

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
        info.getNameToType().put(name, RuleType.TERMINAL);
    }

    @Override
    public void exitNonTerm(GrammarParser.NonTermContext ctx) {
        String name = ctx.LOWER_START_IDENTIFIER().getText();
        String code = null;
        List<Pair<String, String>> inherited = Collections.emptyList();
        List<Pair<String, String>> synthesized = Collections.emptyList();
        List<String> nonTermRule = new ArrayList<>();
        if (ctx.inh() != null) {
            inherited = getAttributes(ctx.inh().attributes());

        }
        if (ctx.synt() != null) {
            synthesized = getAttributes(ctx.synt().attributes());
        }
        if (ctx.nonTermRule().children != null) {
            nonTermRule.addAll(
                    ctx.nonTermRule()
                            .children.stream()
                            .map(ParseTree::getText)
                            .toList()
            );
        }
        if (ctx.BLOCKED_CODE() != null) {
            code = removeFirstLast(ctx.BLOCKED_CODE());
        }
        var nonTerminals =  info.getNonTerminals();
        nonTerminals.putIfAbsent(name, new ArrayList<>());
        nonTerminals.get(name)
                .add(new NonTerminalRule(name, inherited, synthesized, nonTermRule, code));
        info.getNameToType().put(name, RuleType.NON_TERMINAL);
    }

    @Override
    public void exitStartRule(GrammarParser.StartRuleContext ctx) {
        if (ctx.ALL_CODE() != null) {
            String code = ctx.ALL_CODE().getText();
            code = code.substring(3, code.length() - 3);
            info.setCode(code);
        }
    }

    private String removeFirstLast(TerminalNode blockedCode) {
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
