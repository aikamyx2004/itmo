package ru.ainur.generator.code;

import ru.ainur.generator.GrammarInfo;
import ru.ainur.parser.NonTerminalRule;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public class TreeClassesGenerator extends BaseGenerator {

    public TreeClassesGenerator(Path writePath, GrammarInfo info) {
        super(writePath.resolve("%s.java".formatted(info.getTreeClassesClassName())), info);
    }

    protected void generateImpl(BufferedWriter writer) throws IOException {
        writeHeader(writer);
        writeImports(writer);
        writer.write("public class %s {\n".formatted(info.getTreeClassesClassName()));
        for (var t : info.getTerminals()) {
            new TerminalGenerator(info, t, writer).generate();
        }

        for (var e : info.getNonTerminals().entrySet()) {
            var synt = e.getValue().stream()
                    .map(NonTerminalRule::synt)
                    .flatMap(Collection::stream)
                    .distinct()
                    .toList();
            var inh = e.getValue().stream()
                    .map(NonTerminalRule::inh)
                    .flatMap(Collection::stream)
                    .distinct()
                    .toList();
            new NonTerminalGenerator(GeneratorUtil.getNonTerminalSyntClassName(e.getKey()),
                    "extends BaseNonTerminal",
                    true,
                    writer,
                    synt
                    ).generate();
            new NonTerminalGenerator(GeneratorUtil.getNonTerminalInhClassName(e.getKey()),
                    "implements InheritedContext",
                    false,
                    writer,
                    inh
                    ).generate();
        }
        writer.write("}\n");
    }

    private void writeImports(BufferedWriter writer) throws IOException {
        writer.write("import ru.ainur.generator.tree.*;\n");
    }
}
