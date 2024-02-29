package ru.ainur.generator.code;

import ru.ainur.parser.Terminal;

public class GeneratorUtil {
    public static String toCamelCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String getTerminalClassName(Terminal terminal) {
        return toCamelCase(terminal.name());
    }

    public static String getNonTerminalSyntClassName(String name) {
        return toCamelCase(name + "Context");
    }
    public static String getNonTerminalSyntFieldName(String name) {
        return name + "Context";
    }
    public static String getNonTerminalInhClassName(String name) {
        return toCamelCase(name + "Inherited");
    }
    public static String getNonTerminalInhFieldName(String name) {
        return name + "Inherited";
    }
}
