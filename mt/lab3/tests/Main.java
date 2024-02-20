import ru.ainur.parser.JavaBaseListener;
import ru.ainur.parser.JavaParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaListener extends JavaBaseListener {
    public Map<List<String>, List<String>> getVariables() {
        return variables;
    }

    public List<String> getStack() {
        return stack;
    }

    private final Map<List<String>, List<String>> variables = new HashMap<>();
    private final List<String> stack = new ArrayList<>();


    public void exitStackIdentifier(JavaParser.StackIdentifierContext ctx) {
        stack.add(ctx.getText());
    }

    public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        stack.remove(stack.size() - 1);
    }

    public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        stack.remove(stack.size() - 1);
    }

    private void test(String filename) throws IOException {
        test(Path.of(PARSER_TESTS_SRC).resolve(filename + ".java"));
    }

    public void exitVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx) {
        if (ctx == null) {
            return;
        }
        variables.putIfAbsent(stack.stream().toList(), new ArrayList<>());
        variables.get(stack.stream().toList()).add(ctx.Identifier().getText());
    }
}

