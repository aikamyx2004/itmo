package ru.ainur.generator.code;

import ru.ainur.generator.GrammarInfo;
import ru.ainur.parser.NonTermRules;
import ru.ainur.parser.NonTerminal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ParserGenerator extends BaseGenerator {

    public ParserGenerator(Path writePath, GrammarInfo info) {
        super(writePath.resolve("%s.java".formatted(info.getParserClassName())), info);
    }

    protected void generateImpl(BufferedWriter writer) throws IOException {
        writePackage(writer);
        writeHeader(writer);
        writeImports(writer);
        writer.write("public class %s {\n".formatted(info.getParserClassName()));
        writeFields(writer);
        writeConstructor(writer);
        writeMethods(writer);
        writer.write("}\n");
    }

    private void writeHeader(BufferedWriter writer) throws IOException {
        writer.write(info.getHeader());
    }

    private void writeMethods(BufferedWriter writer) throws IOException {
        for (NonTerminal nt : info.getNonTerminals()) {
            writeMethod(nt, writer);
        }
        writer.write("    private TreeToken expect(%s token, BaseNonTerminal nt) throws ParseException {%n".formatted(
                info.getTokenClassName()
        ));

        writer.write("""
                        if (!token.equals(lexer.getCurrentToken())) {
                            throw new ParseException(
                                    "expected %s but found %s".formatted(token.name(), lexer.getCurrentToken().name()),
                                    lexer.getPosition()
                            );
                        }
                        var t = NAME_TO_CTOR.get(token).get();
                        t.setText(lexer.getCurrentTokenString());
                        nt.addChildren(t);
                        lexer.nextToken();
                        return t;
                    }
                """);
    }

    private void writeMethod(NonTerminal nonTerminal, BufferedWriter writer) throws IOException {
        String name = nonTerminal.name();
        String syntClassName = GeneratorUtil.getNonTerminalSyntClassName(name);
        String syntFieldName = GeneratorUtil.getNonTerminalSyntFieldName(name);
        writer.write("    public %s %s(%s %s) throws ParseException {%n".formatted(
                        syntClassName,
                        name,
                        GeneratorUtil.getNonTerminalInhClassName(name),
                        GeneratorUtil.getNonTerminalInhFieldName(name)
                )
        );
        writer.write("        %s %s = new %1$s();\n".formatted(syntClassName, syntFieldName));

        writeSwitchCase(nonTerminal, writer, syntFieldName);
        writer.write("        return %s;%n".formatted(syntFieldName));
        writer.write("    }\n");
    }

    private void writeSwitchCase(NonTerminal nonTerminal, BufferedWriter writer, String syntFieldName) throws IOException {
        writer.write("        switch (lexer.getCurrentToken()) {\n");
        for (var rule : nonTerminal.nonTermRule()) {
            writeCase(rule, nonTerminal.name(), syntFieldName, writer);
        }
        writeDefault(writer);
        writer.write("        }\n");
    }

    private void writeCase(NonTermRules rule, String ntName, String syntFieldName, BufferedWriter writer) throws IOException {
        writer.write(" ".repeat(12));
        List<String> tokens = rule.tokens();
        var firsts = info.countFirst1(tokens, ntName);
        writer.write("case %s -> {\n".formatted(String.join(", ", firsts)));
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            writer.write(" ".repeat(16));
            if (GeneratorUtil.isTerminal(token)) {
                writer.write("var _child%d = expect(%s.%s, %s);%n".formatted(
                        i,
                        info.getTokenClassName(),
                        token,
                        syntFieldName
                ));
            } else {
                String child = "_child%d".formatted(i);
                writer.write("var %s = %s(null);%n".formatted(child, token));
                writer.write(" ".repeat(16));
                writer.write("%s.addChildren(%s);%n"
                        .formatted(
                                syntFieldName,
                                child
                        )
                );
            }
        }
        if (rule.code() != null && !rule.code().isEmpty()) {
            var formattedCode = Arrays.stream(rule.code().strip().split(System.lineSeparator()))
                    .map(t -> " ".repeat(16) + t)
                    .collect(Collectors.joining("\n"));
            writer.write(formattedCode);
            writer.newLine();
        }
        writer.write(" ".repeat(12));
        writer.write("}\n");
    }

    private void writeDefault(BufferedWriter writer) throws IOException {
        writer.write(" ".repeat(12));
        writer.write("default -> throw new ParseException(\"unexpected token: \" + lexer.getCurrentTokenString(), lexer.getPosition());\n");
    }

    private void writeConstructor(BufferedWriter writer) throws IOException {
        writer.write("    public %s(%s lexer) {\n".formatted(info.getParserClassName(), info.getLexerClassName()) +
                "        this.lexer = lexer;\n" +
                "    }\n");
    }

    private void writeImports(BufferedWriter writer) throws IOException {
        writer.write("""
                import ru.ainur.generator.tree.BaseNonTerminal;
                import ru.ainur.generator.tree.TreeToken;
                import %s.%s;
                                
                import java.text.ParseException;
                import java.util.Map;
                import java.util.Set;
                                              
                import static %1$s.%s.*;
                """
                .formatted(info.getPackageName(), info.getLexerClassName(),
                        info.getTreeClassesClassName()
                ));
    }

    private void writeFields(BufferedWriter writer) throws IOException {
//        generateFirstFollowCode("FIRST", info.getFirst(), writer);
//        generateFirstFollowCode("FOLLOW", info.getFirst(), writer);
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
                                        .map(s -> "\"" + s + "\"")
                                        .collect(Collectors.joining(", "))
                        )).collect(Collectors.joining(",\n"));
        writer.write(entries);

        writer.write("\n    );\n\n");
        writer.newLine();
    }
}
