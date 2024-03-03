package ru.ainur.generator.code;

import ru.ainur.generator.info.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class AttributesGenerator {
    private final String name;
    private final BufferedWriter writer;
    private final List<Pair<String, String>> attr;
    private final boolean needConstructor;
    private final String extendsOrImplements;
    private final String tokenName;

    public AttributesGenerator(String className, String tokenName, String extendsOrImplements, boolean needConstructor, BufferedWriter writer, List<Pair<String, String>> attr) {
        this.name = className;
        this.tokenName = tokenName;
        this.writer = writer;
        this.attr = attr;
        this.needConstructor = needConstructor;
        this.extendsOrImplements = extendsOrImplements;
    }

    public void generate() throws IOException {
        writer.write("    public static class %s %s {%n".formatted(name, extendsOrImplements));
        writeConstructor();
        writeFields();
        writer.write("    }\n");
    }


    private void writeFields() throws IOException {
        for (var e : attr) {
            writer.write("        public %s %s;\n".formatted(e.first(), e.second()));
        }
    }


    private void writeConstructor() throws IOException {
        if (!needConstructor) {
            return;
        }
        writer.write("""
                        public %s() {
                            super("%s");
                        }
                """.formatted(name, tokenName));
    }
}
