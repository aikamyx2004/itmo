package ru.ainur.listener;

import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.ainur.parser.JavaBaseListener;
import ru.ainur.parser.JavaParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReplaceListener extends JavaBaseListener {
    private final Map<List<String>, Map<String, String>> variables;
    private final TokenStreamRewriter tokenStreamRewriter;
    private final List<String> stack = new ArrayList<>();

    public ReplaceListener(Map<List<String>, Map<String, String>> variables, JavaParser parser) {
        this.variables = variables;
        this.tokenStreamRewriter = new TokenStreamRewriter(parser.getTokenStream());
    }

    public TokenStreamRewriter getTokenStreamRewriter() {
        return tokenStreamRewriter;
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
