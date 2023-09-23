import expression.*;

import parser.ExpressionParser;
import util.FastScanner;

import java.io.*;
import java.util.*;

public class Task22 {
    public void solve() throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader("input.txt"))) {
            out = new BufferedWriter(new FileWriter("output.txt"));
            FastScanner reader = new FastScanner(in);
            String line = reader.nextLine();
            line = line.substring(0, line.indexOf("|-"));
            int ind = 0;
            for (String expr : line.split(",")) {
                expr = expr.trim();
                if (expr.isEmpty()) {
                    continue;
                }
                Expression e = PARSER.parse(expr);
                hypot.put(e, ind);
                ind++;
            }

            while ((line = reader.nextLine()) != null) {
                if (!line.trim().isEmpty())
                    nextLine(line);
            }
            out.close();
        }
    }

    private void nextLine(String line) {
        cnt++;
        lastExpr = PARSER.parse(line);
        write("(%d) %s ", cnt, lastExpr.toString());
        if (!(checkHypotheses() || checkAxiom() || checkMP())) {
            write("(Не доказано)%n");
        }
        update();
    }

    private void update() {
//        lines.add(lastExpr);
        if (allExpr.get(lastExpr) != null) {
            return;
        }
        allExpr.put(lastExpr, cnt);


        if (lastExpr instanceof Implication) {
            Implication impl = (Implication) lastExpr;
            Expression left = impl.getLeft();
            Integer ind = allExpr.get(left);
            if (ind != null) {
                MPIndexes.put(impl.getRight(), new Pair<>(cnt, ind));
            } else {
                Expression right = impl.getRight();
                possibleMP.putIfAbsent(left, new ArrayList<>());
                possibleMP.get(left).add(new Pair<>(right, cnt));
            }
        }
        for (Pair<Expression, Integer> pair : possibleMP.getOrDefault(lastExpr, EMPTY_LIST)) {
            MPIndexes.put(pair.a, new Pair<>(pair.b, cnt));
        }
        possibleMP.remove(lastExpr);
    }

    private boolean checkHypotheses() {
        Expression line = lastExpr;
        int ind = hypot.getOrDefault(line, -1);
        if (ind != -1) {
            write("(Предп. %d)%n", ind + 1);
            return true;
        }
        return false;
    }

    private boolean checkAxiom() {
        Expression expr = lastExpr;
        for (int i = 0; i < AXIOMS.size(); i++) {
            Expression axiom = AXIOMS.get(i);
            if (isAxiom(axiom, expr, new HashMap<>())) {
                write("(Сх. акс. %d)%n", i + 1);
                return true;
            }
        }
        return false;
    }

    private boolean checkMP() {
        Pair<Integer, Integer> ind = MPIndexes.get(lastExpr);
        if (ind != null) {
            write("(M.P. %d, %d)%n", ind.a, ind.b);
            return true;
        }
        return false;
    }

    private boolean isAxiom(Expression axiom, Expression expr, HashMap<Variable, Expression> varToExpr) {
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
                    isAxiom(binaryAxiom.getLeft(), binaryExpr.getLeft(), varToExpr) &&
                    isAxiom(binaryAxiom.getRight(), binaryExpr.getRight(), varToExpr);
        }
        if (axiom instanceof Negation) {
            Negation negAxiom = (Negation) axiom;
            Negation negExpr = (Negation) expr;
            return isAxiom(negAxiom.getExpr(), negExpr.getExpr(), varToExpr);
        }
        throw new IllegalArgumentException("it is not variable or operator");
    }

    private void write(String line, Object... args) {
        try {
            out.write(String.format(line, args));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final List<Expression> AXIOMS = new ArrayList<>();
    private static final ExpressionParser PARSER = new ExpressionParser();
    private static final List<Pair<Expression, Integer>> EMPTY_LIST = new ArrayList<>();
    //    private final List<Expression> lines = new ArrayList<>();
    private final Map<Expression, Integer> hypot = new HashMap<>();
    private final Map<Expression, List<Pair<Expression, Integer>>> possibleMP = new HashMap<>();
    private final Map<Expression, Pair<Integer, Integer>> MPIndexes = new HashMap<>();

    private final Map<Expression, Integer> allExpr = new HashMap<>();
    private Expression lastExpr;
    private int cnt = 0;
    BufferedWriter out;

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
