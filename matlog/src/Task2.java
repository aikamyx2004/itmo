import expression.*;
import parser.ExpressionParser;
import util.FastScanner;
import util.MPChecker;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Task2 {
    public void solve() throws IOException {
        List<String> lines = new ArrayList<>();

        try (Scanner reader = new Scanner(System.in)) {
            String line;
            while (true) {
//                try {
                    line = reader.nextLine();
//                } catch (IOException e) {
//                    break;
//                }

                if (line == null) {
                    break;
                }
                if (line.trim().isEmpty()) continue;
                lines.add(line);
            }
            out = new BufferedWriter(new PrintWriter(System.out));
            for (String l : lines) {
                nextLine(l);
            }
            out.close();
        }
    }

    private void nextLine(String line) {
        cnt++;
        lastLine = parseLine(line);
        write(String.format("[%d] %s ", cnt, lastLine));
        if (!(checkHypotheses() || checkAxiom() || checkMP() || checkDeduction())) {
            write("[Incorrect]%n");
        }
        update(lastLine);
    }

    private Line parseLine(String line) {
        String[] splitted = line.split("\\|-");
        Expression expr = PARSER.parse(splitted[1]);
        if (!splitted[0].trim().isEmpty()) {
            List<Expression> context = Arrays.stream(splitted[0].split(",")).map(PARSER::parse).collect(Collectors.toList());
            return new Line(new ContextList(context), expr, cnt);
        }
        return new Line(EMPTY_CONTEXT, expr, cnt);
    }

    private boolean checkHypotheses() {
        Line line = lastLine;

        Expression expr = line.getExpression();
        List<Expression> context = line.getContext().getExpressions();
        for (int i = 0; i < context.size(); i++) {
            if (Objects.equals(context.get(i), expr)) {
                write("[Hyp. %d]%n", i + 1);
                return true;
            }
        }
        return false;
    }


    private boolean checkDeduction() {
        Line line = lastLine;
        Expression expr = line.getExpression().copy();
        Map<Expression, Integer> cnt = countContext(line.getContext());

        while (expr instanceof Implication) {
            Implication implExpr = (Implication) expr;
            Expression left = implExpr.getLeft();
            expr = implExpr.getRight();
            cnt.put(left, cnt.getOrDefault(left, 0) + 1);
        }
        Integer order = deductionCommon.getOrDefault(cnt, new HashMap<>()).get(expr);
        if (order != null) {
            write("[Ded. %d]%n", order);
            return true;
        }
        return false;
    }

    private boolean checkAxiom() {
        Expression expr = lastLine.getExpression();
        for (int i = 0; i < AXIOMS.size(); i++) {
            Expression axiom = AXIOMS.get(i);
            if (checkCorrectTree(axiom, expr, new HashMap<>())) {
                write("[Ax. sch. %d]%n", i + 1);
                return true;
            }
        }
        return false;
    }

    private boolean checkCorrectTree(Expression axiom, Expression expr, HashMap<Variable, Expression> varToExpr) {
        if (axiom instanceof Variable) {
            Variable var = (Variable) axiom;
            if (varToExpr.containsKey(var)) {
                return varToExpr.get(var).equals(expr);
            } else {
                varToExpr.put(var, expr);
                return true;
            }
        }
        if (axiom.getClass() != expr.getClass()) {
            return false;
        }
        if (axiom instanceof BinaryOperation) {
            BinaryOperation binaryAxiom = (BinaryOperation) axiom;
            BinaryOperation binaryExpr = (BinaryOperation) expr;
            return binaryAxiom.getOperationSign().equals(binaryExpr.getOperationSign()) &&
                    checkCorrectTree(binaryAxiom.getLeft(), binaryExpr.getLeft(), varToExpr) &&
                    checkCorrectTree(binaryAxiom.getRight(), binaryExpr.getRight(), varToExpr);
        }
        if (axiom instanceof Negation) {
            Negation negAxiom = (Negation) axiom;
            Negation negExpr = (Negation) expr;
            return checkCorrectTree(negAxiom.getExpr(), negExpr.getExpr(), varToExpr);
        }
        throw new IllegalArgumentException("it is not variable or operator");
    }


    private boolean checkMP() {
        Map<Expression, Integer> counter = countContext(lastLine.getContext());
        MPChecker checker = contextToChecker.get(counter);
        if (checker == null) {
            return false;
        }
        Pair<Integer, Integer> indexes = checker.check(lastLine.getExpression());
        if (indexes == null) {
            return false;
        }
        write("[M.P. %d, %d]%n", indexes.a, indexes.b);
        return true;
    }


    private void update(Line line) {
        Map<Expression, Integer> counter = countContext(line.getContext());
        Expression expr = line.getExpression();
        { //update MP
            contextToChecker.putIfAbsent(counter, new MPChecker());
            contextToChecker.get(counter).update(line);
        }

        { //update deduction
            counter = countContext(line.getContext());
            while (expr instanceof Implication) {
                Implication implExpr = (Implication) expr;
                Expression left = implExpr.getLeft();
                expr = implExpr.getRight();
                counter.put(left, counter.getOrDefault(left, 0) + 1);
            }
            deductionCommon.putIfAbsent(counter, new HashMap<>());
            deductionCommon.get(counter).put(expr, cnt);
        }
    }

    private Map<Expression, Integer> countContext(ContextList context) {
        Map<Expression, Integer> count = new HashMap<>();
        for (Expression expr : context.getExpressions()) {
            count.put(expr, count.getOrDefault(expr, 0) + 1);
        }
        return count;
    }

    private void write(String line, Object... args) {
        try {
            out.write(String.format(line, args));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ContextList EMPTY_CONTEXT = new ContextList(new ArrayList<>());
    private static final ExpressionParser PARSER = new ExpressionParser();
    private static final List<Expression> AXIOMS = new ArrayList<>();

    private final Map<Map<Expression, Integer>, Map<Expression, Integer>> deductionCommon = new HashMap<>();
    private Line lastLine;
    private final Map<Map<Expression, Integer>, MPChecker> contextToChecker = new HashMap<>();
    private int cnt = 0;
    private BufferedWriter out;

    static {
        Variable A = new Variable("A");
        Variable B = new Variable("B");
        Variable C = new Variable("C");
        Implication ABImpl = new Implication(A, B);
        Implication BAImpl = new Implication(B, A);
        Implication ACImpl = new Implication(A, C);
        Implication BCImpl = new Implication(B, C);
        Implication ABCImpl = new Implication(A, BCImpl);
        Conjunction ABConj = new Conjunction(A, B);
        Disjunction ABDisj = new Disjunction(A, B);
        Negation notA = new Negation(A);

        AXIOMS.add(new Implication(A, BAImpl));                                                                  // 1
        AXIOMS.add(new Implication(ABImpl, new Implication(ABCImpl, ACImpl)));                                   // 2
        AXIOMS.add(new Implication(A, new Implication(B, ABConj)));                                              // 3
        AXIOMS.add(new Implication(ABConj, A));                                                                  // 4
        AXIOMS.add(new Implication(ABConj, B));                                                                  // 5
        AXIOMS.add(new Implication(A, ABDisj));                                                                  // 6
        AXIOMS.add(new Implication(B, ABDisj));                                                                  // 7
        AXIOMS.add(new Implication(ACImpl, new Implication(BCImpl, new Implication(ABDisj, C))));                // 8                                                   //8
        AXIOMS.add(new Implication(ABImpl, new Implication(new Implication(A, new Negation(B)), notA)));         // 9
        AXIOMS.add(new Implication(new Negation(notA), A));                                                      //10
    }
}

/*
|-B -> (B -> B)
|-(B -> (B -> B)) -> (A -> (B -> (B -> B)))
|-A -> (B -> (B -> B))
* */
