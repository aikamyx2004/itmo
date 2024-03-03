package ru.ainur.test;


import ru.ainur.generator.tree.BaseNonTerminal;
import ru.ainur.generator.tree.InheritedContext;
import ru.ainur.generator.tree.TreeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TestTreeClasses {
    public static final Map<TestToken, Supplier<TreeToken>> NAME_TO_CTOR = new HashMap<>();

    static {
        NAME_TO_CTOR.put(TestToken.SIN, SIN::new);
        NAME_TO_CTOR.put(TestToken.COS, COS::new);
        NAME_TO_CTOR.put(TestToken.TAN, TAN::new);
        NAME_TO_CTOR.put(TestToken.PLUS, PLUS::new);
        NAME_TO_CTOR.put(TestToken.MINUS, MINUS::new);
        NAME_TO_CTOR.put(TestToken.NUMBER, NUMBER::new);
        NAME_TO_CTOR.put(TestToken.POWER, POWER::new);
        NAME_TO_CTOR.put(TestToken.MULTIPLY, MULTIPLY::new);
        NAME_TO_CTOR.put(TestToken.DIVIDE, DIVIDE::new);
        NAME_TO_CTOR.put(TestToken.FUNC, FUNC::new);
        NAME_TO_CTOR.put(TestToken.LPAREN, LPAREN::new);
        NAME_TO_CTOR.put(TestToken.RPAREN, RPAREN::new);
    }

    public static class EOF extends TreeToken {
        public EOF() {
            super("EOF");
        }
    }

    public static class SIN extends TreeToken {
        public SIN() {
            super("SIN");
        }
    }

    public static class COS extends TreeToken {
        public COS() {
            super("COS");
        }
    }

    public static class TAN extends TreeToken {
        public TAN() {
            super("TAN");
        }
    }

    public static class PLUS extends TreeToken {
        public PLUS() {
            super("PLUS");
        }
    }

    public static class MINUS extends TreeToken {
        public MINUS() {
            super("MINUS");
        }
    }

    public static class NUMBER extends TreeToken {
        public NUMBER() {
            super("NUMBER");
        }
    }

    public static class POWER extends TreeToken {
        public POWER() {
            super("POWER");
        }
    }

    public static class MULTIPLY extends TreeToken {
        public MULTIPLY() {
            super("MULTIPLY");
        }
    }

    public static class DIVIDE extends TreeToken {
        public DIVIDE() {
            super("DIVIDE");
        }
    }

    public static class FUNC extends TreeToken {
        public FUNC() {
            super("FUNC");
        }
    }

    public static class LPAREN extends TreeToken {
        public LPAREN() {
            super("LPAREN");
        }
    }

    public static class RPAREN extends TreeToken {
        public RPAREN() {
            super("RPAREN");
        }
    }

    public static class StartRuleContext extends BaseNonTerminal {
        public StartRuleContext() {
            super("startRule");
        }

        public double res;
    }

    public static class StartRuleInherited implements InheritedContext {
    }

    public static class EContext extends BaseNonTerminal {
        public EContext() {
            super("e");
        }

        public double res;
    }

    public static class EInherited implements InheritedContext {
    }

    public static class EPrimeContext extends BaseNonTerminal {
        public EPrimeContext() {
            super("ePrime");
        }

        public double res;
    }

    public static class EPrimeInherited implements InheritedContext {
        public double res;
    }

    public static class TContext extends BaseNonTerminal {
        public TContext() {
            super("t");
        }

        public double res;
    }

    public static class TInherited implements InheritedContext {
    }

    public static class TPrimeContext extends BaseNonTerminal {
        public TPrimeContext() {
            super("tPrime");
        }

        public double res;
    }

    public static class TPrimeInherited implements InheritedContext {
        public double res;
    }

    public static class PContext extends BaseNonTerminal {
        public PContext() {
            super("p");
        }

        public double res;
    }

    public static class PInherited implements InheritedContext {
    }

    public static class PPrimeContext extends BaseNonTerminal {
        public PPrimeContext() {
            super("pPrime");
        }

        public double res;
    }

    public static class PPrimeInherited implements InheritedContext {
        public double res;
    }

    public static class FContext extends BaseNonTerminal {
        public FContext() {
            super("f");
        }

        public double res;
    }

    public static class FInherited implements InheritedContext {
    }
}
