
package ru.ainur.test;

import ru.ainur.generator.tree.*;
public class TestTreeClasses {
    public static class EOF extends TreeToken {
        public EOF() {
            super("EOF");
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

    public static class TContext extends BaseNonTerminal {
        public TContext() {
            super("TContext");
        }
    }
    public static class TInherited implements InheritedContext {
    }
    public static class EContext extends BaseNonTerminal {
        public EContext() {
            super("EContext");
        }
        public int b;
    }
    public static class EInherited implements InheritedContext {
        public int a;
    }
    public static class TPrimeContext extends BaseNonTerminal {
        public TPrimeContext() {
            super("TPrimeContext");
        }
    }
    public static class TPrimeInherited implements InheritedContext {
    }
    public static class FContext extends BaseNonTerminal {
        public FContext() {
            super("FContext");
        }
    }
    public static class FInherited implements InheritedContext {
    }
    public static class StartRuleContext extends BaseNonTerminal {
        public StartRuleContext() {
            super("StartRuleContext");
        }
    }
    public static class StartRuleInherited implements InheritedContext {
    }
    public static class EPrimeContext extends BaseNonTerminal {
        public EPrimeContext() {
            super("EPrimeContext");
        }
        public int res;
    }
    public static class EPrimeInherited implements InheritedContext {
    }
}
