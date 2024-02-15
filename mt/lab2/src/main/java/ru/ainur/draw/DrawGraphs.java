package ru.ainur.draw;

import guru.nidi.graphviz.attribute.Image;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Size;
import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import ru.ainur.parser.ExpressionParser;
import ru.ainur.parser.Tree;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import static guru.nidi.graphviz.model.Factory.*;


public class DrawGraphs {
    private static final ExpressionParser parser = new ExpressionParser();

    public static void main(String[] args) throws ParseException, IOException {
        Graphviz.useEngine(new GraphvizV8Engine(), new GraphvizCmdLineEngine());

        draw("1", "number");
        draw("(1)", "number-in-brackets");
        draw("sin 30", "sin");
        draw("cos (60)", "cos-in-brackets");
        draw("1+2", "add");
        draw("52 * -812", "mul");
        draw("1 + 2 * 3", "add-mul");
        draw("1 * 2 + 3", "mul-add");
    }

    private static void draw(String input, String filename) throws ParseException, IOException {
        Tree tree = parser.parse(input);
        Graphviz.fromString(tree.toDot())
                .render(Format.PNG)
                .toFile(new File("examples/%s.png".formatted(filename)));
    }
}
