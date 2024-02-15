package ru.ainur.parser;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class TestParserPlusMinus extends TestParserBase {
    @Test
    public void testPlus() throws ParseException {
        testPlusMinus("+");
    }

    @Test
    public void testMinus() throws ParseException {
        testPlusMinus("-");
    }

    @Test
    public void testInvalidMinus() {
        testInvalidPlusMinus("-");
    }

    @Test
    public void testInvalidPlus() {
        testInvalidPlusMinus("+");
    }

    @Test
    public void testUnaryMinus() throws ParseException {
        // -1
        // -1 --- F (N ())
        // из-за '-' обернется до E (T (F ("-1")), E' ())
        // E (T (F (MINUS, F (N ())), E' ())
        // E (
        //      T (
        //          F (
        //              MINUS,
        //              F (N ())
        //             ),
        //          T' ()
        //        ),
        //      E' ()
        //   )
        Tree tree = new Tree("E",
                new Tree("T",
                        new Tree("F",
                                new Tree("-"),
                                new Tree("F", new Tree("N"))
                        ),
                        new Tree("T'")
                ),
                new Tree("E'")
        );
        test("-1", tree);
        test("-\t123", tree);
        test("-\t735", tree);
    }

    private void testPlusMinus(String sign) throws ParseException {
        // lets parse: 1 + 2
        // E (T, E') (1 --- T, E' --- + 2)
        // 1 --- N () -> F (N ()) -> T (F (N ()), T' ())
        // 2 -> T (F (N ()), T' ())

        // E' --- E' (+ T, E') --- E' (+ T (F (N ()), T' ()), E' ())
        // 1 + 2 --- E (T (F (N ()), T' ()), E' (+, T (F (N ()), T' ()), E' ()))
        //  E (
        //      T  (
        //            F (N ()),
        //            T' ()
        //         ),
        //      E' (
        //            +,
        //            T (
        //                 F (N ()),
        //                 T' ()
        //              ),
        //            E' ()
        //         )
        //     )
        Tree tree = new Tree(
                "E",
                new Tree(
                        "T",
                        new Tree("F", new Tree("N")),
                        new Tree("T'")
                ),
                new Tree("E'",
                        new Tree(sign),
                        new Tree("T",
                                new Tree("F", new Tree("N")),
                                new Tree("T'")
                        ),
                        new Tree("E'")
                )
        );


        test("1\r " + sign + " 2", tree);
        test("12 " + sign + "\t 2", tree);
        test("52 " + sign + " 812\t", tree);
        test("\t735234  " + sign + "\r 112313", tree);
    }


    public void testInvalidPlusMinus(String symbol) {
        testInvalid(symbol);
        testInvalid("\t100 %s".formatted(symbol));
        testInvalid("%s %s".formatted(symbol, symbol));
        testInvalid("100  %s 52 %s ".formatted(symbol, symbol));
    }
}
