package ru.ainur.generator.tree;

import java.util.List;

public class BaseNonTerminal implements ParseTree {
    private final String name;
    private List<ParseTree> children;

    public BaseNonTerminal(String name) {
        this.name = name;
    }


    public int fillDot(StringBuilder sb, int index) {
        int current = index;
        sb.append("\t%d [label=\"%s\"]%n".formatted(current, name));

        for (ParseTree tree : children) {
            index++;
            sb.append("\t%d -> %d%n".formatted(current, index));
            index = tree.fillDot(sb, index);
        }
        return index;
    }

    public void addChildren(ParseTree t) {
        children.add(t);
    }
}
