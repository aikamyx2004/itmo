package util;

public enum Rule {
    AXIOM("Ax"),
    MP("E->"),
    DEDUCTION("I->"),
    AND("I&"),
    LEFT_FROM_AND("El&"),
    RIGHT_FROM_AND("Er&"),
    LEFT_OR("Il|"),
    RIGHT_OR("Ir|"),
    TRIPLE("E|"),
    REMOVE_NOT("E!!");
    private final String name;

    Rule(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
