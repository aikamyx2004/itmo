package ru.ainur;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.ainur.parser.JavaBaseListener;
import ru.ainur.parser.JavaParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WriteListener extends JavaBaseListener {
    private final Map<List<String>, Map<String, String>> variables;
    private final CommonTokenStream tokenStream;
    private final List<String> stack = new ArrayList<>();

    public WriteListener(Map<List<String>, Map<String, String>> variables, CommonTokenStream tokenStream) {
        this.variables = variables;
        this.tokenStream = tokenStream;
    }

    @Override
    public void exitVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx) {
        if (ctx == null) {
            return;
        }
        replaceName(ctx.Identifier());
    }

    private void replaceName(TerminalNode identifier) {
        String curName = identifier.getText();
        int index = identifier
                .getSymbol()
                .getTokenIndex();

        for (int i = 0; i < stack.size(); i++) {
            var names = variables.get(stack.subList(0, stack.size() - i));
            if (names == null) {
                continue;
            }
            String newName = names.get(curName);
            if (newName != null) {
                CommonToken token = getToken(index);
                token.setText(newName);
                return;
            }
        }


    }

    private CommonToken getToken(int index) {
        Token t = tokenStream.get(index);
        if (t instanceof CommonToken commonToken) {
            return commonToken;
        } else {
            throw new RuntimeException("Token is not common");
        }
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


}
