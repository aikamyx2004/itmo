package ru.ainur.generator.code;

import ru.ainur.generator.GrammarInfo;
import ru.ainur.parser.NonTerminalRule;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ParserGenerator extends BaseGenerator {

    public ParserGenerator(Path writePath, GrammarInfo info) {
        super(writePath.resolve("%s.java".formatted(info.getParserClassName())), info);
    }

    protected void generateImpl(BufferedWriter writer) throws IOException {
        writeHeader(writer);
        writeImports(writer);
        writer.write("public class %s {\n".formatted(info.getParserClassName()));
        writeFields(writer);
        writeConstructor(writer);
        writeMethods(writer);
        writer.write("}\n");
    }

    private void writeMethods(BufferedWriter writer) throws IOException {
        for(var nt : info.getNonTerminals().entrySet()){
            writeMethod(nt.getKey(), nt.getValue(), writer);
        }
    }

    private void writeMethod(String name, List<NonTerminalRule> rules, BufferedWriter writer) throws IOException {
        writer.write("    public %s %s(%s %s) {%n".formatted(
                GeneratorUtil.getNonTerminalSyntClassName(name),
                name,
                GeneratorUtil.getNonTerminalInhClassName(name),
                GeneratorUtil.getNonTerminalInhFieldName(name)
            )
        );

        writer.write("    }\n");
    }

    private void writeConstructor(BufferedWriter writer) throws IOException {
        writer.write("    public %s(%s lexer) {\n".formatted(info.getParserClassName(), info.getLexerClassName()) +
                "        this.lexer = lexer;\n" +
                "    }\n");
    }

    private void writeImports(BufferedWriter writer) throws IOException {
        writer.write("""
                import ru.ainur.test.TestTreeClasses.*;
                                
                import java.util.Map;
                import java.util.Set;
                
                """);
    }

    private void writeFields(BufferedWriter writer) throws IOException {
        generateFirstFollowCode("FIRST", info.getFirst(), writer);
        generateFirstFollowCode("FOLLOW", info.getFirst(), writer);
        writer.write("    private final %s lexer;%n".formatted(info.getLexerClassName()));
    }
    public static void generateFirstFollowCode(String name, Map<String, Set<String>> data, BufferedWriter writer) throws IOException {
        writer.write("    private static final Map<String, Set<String>> %s = Map.ofEntries(".formatted(name));
        writer.newLine();
        String entries = data.entrySet()
                .stream().map(e ->
                        "        Map.entry(\"%s\", Set.of(%s))".formatted(
                        e.getKey(),
                        e.getValue().stream()
                                .map(s-> "\""+s+"\"")
                                .collect(Collectors.joining(", "))
                )).collect(Collectors.joining(",\n"));
        writer.write(entries);

        writer.write("\n    );\n\n");
        writer.newLine();
    }
}
