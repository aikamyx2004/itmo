package ru.ainur.generator.tree;

public class TreeToken implements ParseTree {
    private String name;
    private String text;

    public TreeToken(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int fillDot(StringBuilder sb, int index) {
        sb.append("\t%d [label=\"%s\"]%n".formatted(index, text));
        return index;
    }
}
