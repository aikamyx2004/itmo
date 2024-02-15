package ru.ainur.parser;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

import guru.nidi.graphviz.model.Factory;

public class Tree {
    private final String node;
    private final List<Tree> children;

    public Tree(String node, Tree... children) {
        this.node = node;
        this.children = new ArrayList<Tree>(Arrays.asList(children));
    }

    public Tree(String node) {
        this.node = node;
        this.children = new ArrayList<>();
    }

    public void addChildren(Tree t) {
        children.add(t);
    }

    public String getNode() {
        return node;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph expr {");
        sb.append(System.lineSeparator());

        fillDot(sb, 0);

        sb.append('}');
        return sb.toString();
    }

    private int fillDot(StringBuilder sb, int index) {
        int current = index;
        sb.append("\t%d [label=\"%s\"]%n".formatted(current, node));

        for (Tree tree : children) {
            index++;
            sb.append("\t%d -> %d%n".formatted(current, index));
            index = tree.fillDot(sb, index);
        }
        return index;
    }


    @Override
    public String toString() {
        return "%s (%s)".formatted(
                node,
                children.stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(","))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tree tree = (Tree) o;
        return Objects.equals(node, tree.node) && Objects.equals(children, tree.children);
    }


    @Override
    public int hashCode() {
        return Objects.hash(node, children);
    }
}
