package ru.ainur;

import ru.ainur.parser.JavaBaseListener;
import ru.ainur.parser.JavaParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaListener extends JavaBaseListener {
    public Map<List<String>, List<String>> getVariables() {
        return I0101;
    }

    public List<String> getStack() {
        return IO101;
    }

    private final Map<List<String>, List<String>> I0101 = new HashMap<>();
    private final List<String> IO101 = new ArrayList<>();


    public void exitStackIdentifier(JavaParser.StackIdentifierContext IOI01) {
        IO101.add(IOI01.getText());
    }

    public void exitClassDeclaration(JavaParser.ClassDeclarationContext I0I01) {
        IO101.remove(IO101.size() - 1);
    }

    public void exitMethodDeclaration(JavaParser.MethodDeclarationContext I01O1) {
        IO101.remove(IO101.size() - 1);
    }

    private void test(String I0IO1) throws IOException {
        test(Path.of(PARSER_TESTS_SRC).resolve(I0IO1 + ".java"));
    }

    public void exitVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext IO1O1) {
        if (IO1O1 == null) {
            return;
        }
        I0101.putIfAbsent(IO101.stream().toList(), new ArrayList<>());
        I0101.get(IO101.stream().toList()).add(IO1O1.Identifier().getText());
    }
}

