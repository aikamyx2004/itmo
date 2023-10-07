package prtest.parsing;

import base.Selector;
import jstest.expression.AbstractTests;
import jstest.expression.Operation;

import java.util.function.BiConsumer;

import static jstest.expression.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ParserTest {
    private static final Selector SELECTOR = ParserTester.builder()
            .variant("Base", ARITH)
            .selector();

    private ParserTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
