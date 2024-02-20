package ru.ainur;

import ru.ainur.parser.JavaBaseListener;
import ru.ainur.parser.JavaParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaListener extends JavaBaseListener {
    public Map<List<String>, List<String>> getVariables() {
        return I010;
    }

    public List<String> getStack() {
        return IO10;
    }

    private final Map<List<String>, List<String>> I010 = new HashMap<>();
    private final List<String> IO10 = new ArrayList<>();


    public void exitStackIdentifier(JavaParser.StackIdentifierContext IOI0) {
        IO10.add(IOI0.getText());
    }

    public void exitClassDeclaration(JavaParser.ClassDeclarationContext I0I0) {
        IO10.remove(IO10.size() - 1);
    }

    public void exitMethodDeclaration(JavaParser.MethodDeclarationContext I01O) {
        IO10.remove(IO10.size() - 1);
    }

    private void test(String I0IO) throws IOException {
        test(Path.of(PARSER_TESTS_SRC).resolve(I0IO + ".java"));
    }

    public void exitVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext IO1O) {
        if (IO1O == null) {
            return;
        }
        I010.putIfAbsent(IO10.stream().toList(), new ArrayList<>());
        I010.get(IO10.stream().toList()).add(IO1O.Identifier().getText());
    }
}

