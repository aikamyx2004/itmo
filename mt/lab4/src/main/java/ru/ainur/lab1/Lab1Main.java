package ru.ainur.lab1;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.engine.GraphvizV8Engine;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class Lab1Main {
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
        draw("(1+2)*sin(-3*(7-4)+2)", "example");
    }

    private static void draw(String input, String filename) throws ParseException, IOException {
        var tree = parse(input);
        Graphviz.fromString(tree.toDot())
                .render(Format.PNG)
                .toFile(new File("img/lab1/%s.png".formatted(filename)));
    }

    private static Lab1TreeClasses.EContext parse(String input) throws ParseException {
        Lab1Lexer lexer = new Lab1Lexer(input);
        Lab1Parser parser = new Lab1Parser(lexer);
        return parser.e(new Lab1TreeClasses.EInherited());
    }
}
