package util;

import expression.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Proover {
    public Proover(int n, Expression expr, BufferedWriter out, ArrayList<String> variables) {
        this.n = n;
        this.expr = expr;
        this.out = out;
        this.variables = variables;
    }

    public void proofAll() throws IOException {
        createSimpleTree(0);
    }

    private void createSimpleTree(int ind) throws IOException {
        if (ind == n) {
            this.depth = n;
            startProof();
            return;
        }
        String v = variables.get(ind);

        addContext.add(v);
        evaluation.put(v, Boolean.TRUE);
        createSimpleTree(ind + 1);
        addContext.remove(addContext.size() - 1);

        addContext.add("(" + v + "->" + FALSE + ")");
        evaluation.put(v, Boolean.FALSE);
        createSimpleTree(ind + 1);
        addContext.remove(addContext.size() - 1);


        proofAOrNotA(ind + 1, v);
        writeLine(ind, expr.toString(), Rule.TRIPLE);
    }

    private void proofAOrNotA(int d, String v) throws IOException {
        Variable V = new Variable(v);
        Implication NotV = new Implication(V, FALSE_EXPR);
        Disjunction All = new Disjunction(V, NotV);
        Implication NotAll = new Implication(All, FALSE_EXPR);

        addContext.add(NotAll.toString());
        writeLine(d + 2, NotAll.toString(), Rule.AXIOM);

        addContext.add(V.toString());
        writeLine(d + 5, NotAll.toString(), Rule.AXIOM);
        writeLine(d + 6, V.toString(), Rule.AXIOM);
        writeLine(d + 5, All.toString(), Rule.LEFT_OR);

        writeLine(d + 4, FALSE, Rule.MP);
        addContext.remove(v);

        writeLine(d + 3, NotV.toString(), Rule.DEDUCTION);
        writeLine(d + 2, All.toString(), Rule.RIGHT_OR);


        writeLine(d + 1, FALSE, Rule.MP);
        addContext.remove(addContext.size() - 1);

        writeLine(d, All.toString(), Rule.REMOVE_NOT);
    }


    private void startProof() throws IOException {
        expr.fillEvaluation(exprEvaluation, evaluation);
        if (!proof(depth, expr)) {
            throw new RuntimeException("wtf it is false expr");
        }
    }

    private boolean proof(int depth, Expression expr) throws IOException {
        if (expr instanceof False) {
            addContext.add(FALSE);
            writeLine(depth + 1, FALSE, Rule.AXIOM);
            addContext.remove(addContext.size() - 1);
            writeLineNeg(depth, FALSE, Rule.DEDUCTION);
            return false;
        }
        if (expr instanceof Variable) {
            Variable v = (Variable) expr;
            Implication i = new Implication(expr, FALSE_EXPR);
            boolean eval = evaluation.get(v.getVariable());
            writeLine(depth, eval ? v.toString() : i.toString(), Rule.AXIOM);
            return eval;
        }
        if (expr instanceof Conjunction) {
            return proofConj(depth, (Conjunction) expr);
        }
        if (expr instanceof Disjunction) {
            return proofDisj(depth, (Disjunction) expr);
        }
        if (expr instanceof Implication) {
            return proofImpl(depth, (Implication) expr);
        }
        throw new RuntimeException("wtf unexpected operation");
    }

    private boolean proofConj(int depth, Conjunction c) throws IOException {
        Expression left = c.getLeft();
        Expression right = c.getRight();
        boolean leftRes = exprEvaluation.get(left);
        boolean rightRes = exprEvaluation.get(right);
        boolean res = leftRes & rightRes;
        if (leftRes) {
            if (rightRes) {
                //ok
                proof(depth + 1, left);
                proof(depth + 1, right);
                writeLine(depth, c.toString(), Rule.AND);
            } else {
                addContext.add(c.toString());
                proof(depth + 2, right); //right->0

                writeLine(depth + 3, c.toString(), Rule.AXIOM);
                writeLine(depth + 2, right.toString(), Rule.RIGHT_FROM_AND);

                writeLine(depth + 1, FALSE, Rule.MP);
                addContext.remove(addContext.size() - 1);
                writeLineNeg(depth, c.toString(), Rule.DEDUCTION);
            }
            return res;
        }
        if (rightRes) {
            addContext.add(c.toString());
            proof(depth + 2, left);//left->0

            writeLine(depth + 3, c.toString(), Rule.AXIOM);
            writeLine(depth + 2, left.toString(), Rule.LEFT_FROM_AND);

            writeLine(depth + 1, FALSE, Rule.MP);
            addContext.remove(addContext.size() - 1);
            writeLineNeg(depth, c.toString(), Rule.DEDUCTION);
            return res;
        }

        addContext.add(c.toString());
        proof(depth + 2, right); //right->0

        writeLine(depth + 3, c.toString(), Rule.AXIOM);
        writeLine(depth + 2, right.toString(), Rule.RIGHT_FROM_AND);

        writeLine(depth + 1, FALSE, Rule.MP);
        addContext.remove(addContext.size() - 1);
        writeLineNeg(depth, c.toString(), Rule.DEDUCTION);
        return res;
    }


    private boolean proofDisj(int depth, Disjunction d) throws IOException {
        Expression left = d.getLeft();
        Expression right = d.getRight();
        boolean leftRes = exprEvaluation.get(left);
        boolean rightRes = exprEvaluation.get(right);
        boolean res = leftRes | rightRes;
        if (leftRes) {
            proof(depth + 1, left);
            writeLine(depth, d.toString(), Rule.LEFT_OR);
        } else if (rightRes) {
            proof(depth + 1, right);
            writeLine(depth, d.toString(), Rule.RIGHT_OR);
        } else {
            addContext.add(d.toString());
            {
                {
                    addContext.add(left.toString());
                    proof(depth + 3, left); //left->0
                    writeLine(depth + 3, left.toString(), Rule.AXIOM);
                    writeLine(depth + 2, FALSE, Rule.MP);
                    addContext.remove(addContext.size() - 1);
                }
                //?if (left != right)
                {
                    addContext.add(right.toString()); //right->0
                    proof(depth + 3, right);
                    writeLine(depth + 3, right.toString(), Rule.AXIOM);
                    writeLine(depth + 2, FALSE, Rule.MP);
                    addContext.remove(addContext.size() - 1);
                }
                {
                    writeLine(depth + 2, d.toString(), Rule.AXIOM);
                }
            }
            writeLine(depth + 1, FALSE, Rule.TRIPLE);
            addContext.remove(addContext.size() - 1);
            writeLineNeg(depth, d.toString(), Rule.DEDUCTION);

        }
        return res;
    }

    private boolean proofImpl(int depth, Implication i) throws IOException {
        Expression left = i.getLeft();
        Expression right = i.getRight();
        boolean leftRes = exprEvaluation.get(left);
        boolean rightRes = exprEvaluation.get(right);
        boolean res = (!leftRes) | rightRes;
        if (rightRes) {
            addContext.add(left.toString());
            proof(depth + 1, right);
            addContext.remove(addContext.size() - 1);
            writeLine(depth, i.toString(), Rule.DEDUCTION);
        } else if (!leftRes) {
            addContext.add(left.toString());
            addContext.add("(" + right.toString() + "->" + FALSE + ")");

            proof(depth + 3, left);//left->0
            writeLine(depth + 3, left.toString(), Rule.AXIOM);

            writeLine(depth + 2, FALSE, Rule.MP);
            addContext.remove(addContext.size() - 1);
            writeLine(depth + 1, right.toString(), Rule.REMOVE_NOT);
            addContext.remove(addContext.size() - 1);
            writeLine(depth, i.toString(), Rule.DEDUCTION);
        } else {
            addContext.add(i.toString());


            proof(depth + 2, right); //right->0
            {
                writeLine(depth + 3, i.toString(), Rule.AXIOM);//l->r
                proof(depth + 3, left); //left
                writeLine(depth + 2, right.toString(), Rule.MP);//r
            }


            writeLine(depth + 1, FALSE, Rule.MP);
            addContext.remove(addContext.size() - 1);
            writeLineNeg(depth, i.toString(), Rule.DEDUCTION);
        }
        return res;
    }

    private void writeLine(int depth, String expr, Rule rule) throws IOException {
        out.write(String.format("[%d] %s|-%s [%s]%n", depth, String.join(",", addContext), expr, rule));
    }

    private void writeLineNeg(int depth, String expr, Rule rule) throws IOException {
        writeLine(depth, "(" + expr + "->" + FALSE + ")", rule);
//        out.write(String.format("[%d] ", depth));

//        out.write('[');
//        out.write(Integer.toString(depth));
//        out.write("] ");
//        out.write(String.join(",", addContext));
//        out.write(String.format("|-(%s->%s) [%s]%n", expr, FALSE, rule));
//        out.write("|-(");
//        out.write(expr);
//        out.write("->");
//        out.write(FALSE);
//        out.write(')');
//        out.write(" [");
//        out.write(rule.toString());
//        out.write(']');
//        out.newLine();
    }

    private static final False FALSE_EXPR = new False();
    private static final String FALSE = "_|_";
    private int depth;
    private BufferedWriter out;
    private Map<String, Boolean> evaluation = new HashMap<>();
    private ArrayList<String> variables;
    private Expression expr;
    private final List<String> addContext = new ArrayList<>();
    private final Map<Expression, Boolean> exprEvaluation = new HashMap<>();
    private final int n;
}
