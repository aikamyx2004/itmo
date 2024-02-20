package ru.ainur.listener;

import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.ainur.parser.JavaBaseListener;
import ru.ainur.parser.JavaParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class ReplaceListener extends JavaBaseListener {
    private static final Random random = new Random();
    private static final List<Supplier<String>> randomBlockActions = List.of(
            () -> "int int_%d = %d;%n".formatted(boundedRandInt(), randInt()),
            () -> "double double_%d = %d;%n".formatted(boundedRandInt(), randInt()),
            () -> "if (false)\n\t{System.out.println(\"foool\");\n\t}\n"
    );

    private static final List<Supplier<String>> randomClasses = List.of(
            () -> "class FoolClass_%d%n{%n}".formatted(boundedRandInt()),
            () -> "enum FoolEnum_%d{\n".formatted(boundedRandInt()) +
                    "    None,\n" +
                    "    YOU,\n" +
                    "    ARE,\n" +
                    "    Fool\n" +
                    "}"
    );

    private final Map<List<String>, Map<String, String>> variables;
    private final TokenStreamRewriter tokenStreamRewriter;
    private final List<String> stack = new ArrayList<>();

    private static int boundedRandInt() {
        return random.nextInt(10000);
    }

    private static int randInt() {
        return random.nextInt();
    }

    public ReplaceListener(Map<List<String>, Map<String, String>> variables, JavaParser parser) {
        this.variables = variables;
        this.tokenStreamRewriter = new TokenStreamRewriter(parser.getTokenStream());
    }

    public TokenStreamRewriter getTokenStreamRewriter() {
        return tokenStreamRewriter;
    }

    @Override
    public void exitBlockStatement(JavaParser.BlockStatementContext ctx) {
        if (ctx.statement() != null && ctx.statement().breakStatement() != null) {
            return;
        }
        if (random.nextBoolean()) {
            String action = randomBlockActions.get(random.nextInt(randomBlockActions.size())).get();
            action = action + "\t".repeat(stack.size());
            tokenStreamRewriter.insertAfter(ctx.stop, action);
        }
    }

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        if (random.nextBoolean()) {
            String action = randomClasses.get(random.nextInt(randomClasses.size())).get();
            action = action + "\t".repeat(stack.size());
            tokenStreamRewriter.insertAfter(ctx.stop, action);
        }
    }

    @Override
    public void exitVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx) {
        if (ctx == null) {
            return;
        }
        replaceName(ctx.Identifier());
    }

    @Override
    public void exitPrimary(JavaParser.PrimaryContext ctx) {
        if (ctx.Identifier() != null) {
            replaceName(ctx.Identifier());
        }
    }

    @Override
    public void exitStackIdentifier(JavaParser.StackIdentifierContext ctx) {
        stack.add(ctx.getText());
    }

    @Override
    public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        stack.remove(stack.size() - 1);
    }

    @Override
    public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        stack.remove(stack.size() - 1);
    }

    private void replaceName(TerminalNode identifier) {
        String curName = identifier.getText();

        for (int i = 0; i < stack.size(); i++) {
            var names = variables.get(stack.subList(0, stack.size() - i));
            if (names == null) {
                continue;
            }
            String newName = names.get(curName);
            if (newName != null) {
                tokenStreamRewriter.replace(identifier.getSymbol(), newName);
                return;
            }
        }

    }
}
