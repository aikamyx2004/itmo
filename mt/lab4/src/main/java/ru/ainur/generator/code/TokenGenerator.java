package ru.ainur.generator.code;

import ru.ainur.generator.GrammarInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class TokenGenerator extends BaseGenerator {

    public TokenGenerator(Path writePath, GrammarInfo info) {
        super(writePath.resolve("%s.java".formatted(info.getTokenClassName())), info);
    }

    protected void generateImpl(BufferedWriter writer) throws IOException {
            writeHeader(writer);
            writer.write("public enum %s {\n".formatted(info.getTokenClassName()));
            writer.write(info.getTerminals().stream()
                    .map(t -> "    " + t.name())
                    .collect(Collectors.joining(",\n")));
            writer.newLine();
            writer.write("}\n");
            writer.newLine();
    }
}
