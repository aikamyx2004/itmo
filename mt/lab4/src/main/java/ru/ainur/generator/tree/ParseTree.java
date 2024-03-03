package ru.ainur.generator.tree;

public interface ParseTree {
    int fillDot(StringBuilder sb, int index);

    default String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph expr {");
        sb.append(System.lineSeparator());

        fillDot(sb, 0);
        sb.append("}");

        return sb.toString();
    }
}
