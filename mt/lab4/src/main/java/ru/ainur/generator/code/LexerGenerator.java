package ru.ainur.generator.code;

import ru.ainur.generator.GrammarInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class LexerGenerator extends BaseGenerator {
    public LexerGenerator(Path writePath, GrammarInfo info) {
        super(writePath.resolve("%s.java".formatted(info.getLexerClassName())), info);
    }

    protected void generateImpl(BufferedWriter writer) throws IOException {
        writeHeader(writer);
        writeImports(writer);
        writer.write("public class %s {\n".formatted(info.getLexerClassName()));
        writeFields(writer);

        writeConstructor(writer);
        writeMethods(writer);

        writer.write("}\n");
    }

    private void writeMethods(BufferedWriter writer) throws IOException {
        writeNextToken(writer);
        writeGetters(writer);
    }

    private void writeGetters(BufferedWriter writer) throws IOException {
        writer.write(
                "    public %s getCurrentToken() {\n".formatted(info.getTokenClassName()) +
                        "        return currentToken;\n" +
                        "    }\n" +
                        "\n" +
                        "    public String getCurrentTokenString() {\n" +
                        "        return currentTokenString;\n" +
                        "    }\n" +
                        "\n" +
                        "    public int getPosition(){\n" +
                        "        return matcher.start();\n" +
                        "    }\n\n");
    }

    private void writeNextToken(BufferedWriter writer) throws IOException {
        writer.write(
                "    public void nextToken() throws ParseException {\n" +
                        "        if (!matcher.find()) {\n" +
                        "            if (currentToken != %s.EOF) {\n".formatted(info.getTokenClassName()) +
                        "                throw new ParseException(\"it is not token\", matcher.start());\n" +
                        "            }\n" +
                        "        }\n" +
                        "        for (int i = 0; i < TERMINALS.size(); i++) {\n" +
                        "            String match = matcher.group(TERMINALS.get(i).name());\n" +
                        "            if (match != null) {\n" +
                        "                currentToken = %s.values()[i];\n".formatted(info.getTokenClassName()) +
                        "                currentTokenString = match;\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n\n");
    }

    private void writeConstructor(BufferedWriter writer) throws IOException {
        writer.write("    public %s(String input) throws ParseException {\n".formatted(info.getLexerClassName()) +
                "        this.input = input;\n" +
                "        this.regex = Pattern.compile(\n" +
                "                TERMINALS.stream()\n" +
                "                        .map(t -> \"(?<%s>%s)\".formatted(t.name(), t.regex()))\n" +
                "                        .collect(Collectors.joining(\"|\"))\n" +
                "        );\n" +
                "        matcher = regex.matcher(input);\n" +
                "        nextToken();\n" +
                "    }\n\n");
    }

    private void writeImports(BufferedWriter writer) throws IOException {
        writer.write(
                """
                        import ru.ainur.parser.Terminal;

                        import java.util.List;
                        import java.util.regex.Matcher;
                        import java.util.regex.Pattern;
                        import java.text.ParseException;
                        import java.util.stream.Collectors;

                        """);
    }

    private void writeFields(BufferedWriter writer) throws IOException {
        writeTerminalsField(writer);
        writer.write("    private final String input;\n");
        writer.write("    private final Pattern regex;\n");
        writer.write("    private final Matcher matcher;\n");
        writer.write("    private %s currentToken;\n".formatted(info.getTokenClassName()));
        writer.write("    private String currentTokenString;\n");
        writer.newLine();
    }

    private void writeTerminalsField(BufferedWriter writer) throws IOException {
        writer.write("    public static final List<Terminal> TERMINALS = List.of(\n");
        writer.write(
                info.getTerminals().stream()
                        .map(t -> "            new Terminal(\"%s\", \"%s\")"
                                .formatted(t.name(), t.regex())
                        ).collect(Collectors.joining(",\n")));

        writer.write("\n    );\n");
    }
}
