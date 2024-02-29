package ru.ainur.generator.code;

import ru.ainur.generator.GrammarInfo;
import ru.ainur.parser.Terminal;

import java.io.BufferedWriter;
import java.io.IOException;

public class TerminalGenerator {
    private final GrammarInfo info;
    private final Terminal terminal;
    private  final BufferedWriter writer;
    public TerminalGenerator(GrammarInfo info, Terminal terminal, BufferedWriter writer) {
        this.info = info;
        this.terminal = terminal;
        this.writer = writer;
    }

    public void generate() throws IOException {
        writer.write("    public static class %s extends TreeToken {%n".formatted(GeneratorUtil.getTerminalClassName(terminal)));
        writeConstructor();
        writer.write("    }\n\n");
    }

    private void writeConstructor() throws IOException {
        writer.write("""
                        public %1$s() {
                            super("%1$s");
                        }
                """.formatted(terminal.name()));
    }
}
