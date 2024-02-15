package ru.ainur.parser;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class TestParserFunction extends TestParserBase {
    @Test
    public void testFunction() throws ParseException {
        // f(1)
        // E (T?, E' ())
        // E (T (F?, T' ()), E' ())
        // E (T (F (FUNC, F?), T' ()), E' ())
        // E (T (F (FUNC, F (N)), T' ()), E' ())
        // E (
        //      T (
        //          F (FUNC, F (N)),
        //          T' ()
        //        ),
        //      E' ()
        //   )

        Tree tree = new Tree("E",
                new Tree("T",
                        new Tree("F", new Tree("FUNC"), new Tree("F", new Tree("N"))),
                        new Tree("T'")
                ),
                new Tree("E'")
        );

        test("abs 12", tree);
        test("sin 15", tree);
        test("f -12", tree);
        test("f -12", tree);
    }
}
