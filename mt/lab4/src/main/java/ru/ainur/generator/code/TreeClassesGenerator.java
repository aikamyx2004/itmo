package ru.ainur.generator.code;

import ru.ainur.generator.GrammarInfo;
import ru.ainur.parser.NonTerminalRule;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

public class TreeClassesGenerator extends BaseGenerator {

    public TreeClassesGenerator(Path writePath, GrammarInfo info) {
        super(writePath.resolve("%s.java".formatted(info.getTreeClassesClassName())), info);
    }

    protected void generateImpl(BufferedWriter writer) throws IOException {
        writeHeader(writer);
        writeImports(writer);
        writer.write("public class %s {\n".formatted(info.getTreeClassesClassName()));

        generateMapToCtor(writer);

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
            new AttributesGenerator(GeneratorUtil.getNonTerminalSyntClassName(e.getKey()),
                    e.getKey(),
                    "extends BaseNonTerminal",
                    true,
                    writer,
                    synt
            ).generate();
            new AttributesGenerator(GeneratorUtil.getNonTerminalInhClassName(e.getKey()),
                    e.getKey(),
                    "implements InheritedContext",
                    false,
                    writer,
                    inh
            ).generate();
        }
        writer.write("}\n");
    }

    private void generateMapToCtor(BufferedWriter writer) throws IOException {
        writer.write("    public static final Map<%s, Supplier<TreeToken>> NAME_TO_CTOR = new HashMap<>();\n".formatted(info.getTokenClassName()));
        writer.write("    static {\n");
        var terminals = info.getTerminals();
        writer.write(terminals.subList(1, terminals.size()).stream()
                .map(t -> "        NAME_TO_CTOR.put(%s.%s, %s::new);\n"
                        .formatted(info.getTokenClassName(), t.name(), GeneratorUtil.getTerminalClassName(t))
                )
                .collect(Collectors.joining("")));
        writer.write("    }\n");
    }

    private void writeImports(BufferedWriter writer) throws IOException {
        writer.write("""
                import ru.ainur.generator.tree.BaseNonTerminal;
                import ru.ainur.generator.tree.InheritedContext;
                import ru.ainur.generator.tree.TreeToken;
                                
                import java.util.HashMap;
                import java.util.Map;
                import java.util.function.Supplier;
                         
                """);
    }
}
