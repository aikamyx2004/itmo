package ru.ainur.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class TestParserSimple extends TestParserBase {

    @Test
    public void testEmpty() throws ParseException {
        Assertions.assertEquals(new Tree("E"), parse(""));
    }

    @Test
    public void testInvalidExpressions() throws ParseException {
        testInvalid("100_000");
        testInvalid("$100");
        testInvalid("52 \t% 12");
        testInvalid("735     & 812");
    }

    @Test
    public void testNumber() throws ParseException {
        // 1 --- N () -> F (N ()) -> T (F (N ()), T' ()) -> E (T (F (N ()), T' ()), E' ())
        // E (
        //      T (
        //          F (N ()),
        //          T' ()
        //        ),
        //      E' ()
        //   )
        Tree tree = new Tree(
                "E",
                new Tree(
                        "T",
                        new Tree("F", new Tree("N")),
                        new Tree("T'")
                ),
                new Tree("E'")
        );

        test("0", tree);
        test("\t12", tree);
        test("      10013 ", tree);
        test("\r\r 735812", tree);
    }

    @Test
    public void testBracketsWithNumber() throws ParseException {
        // (1) - number;
        // F (LPAREN (), E?, RPAREN ())
        // 1 parses like: E (T (F (N ()), T' ()), E' ()) ->
        // -> F (LPAREN (), E?, RPAREN ()) --- F (LPAREN (), E (T (F (N ()), T' ()), E' ()), RPAREN ()) ->
        // E (T ("F (LPAREN (), E (T (F (N ()), T' ()), E' ()), RPAREN ())", T' ()), E' ())
        // E (
        //      T (
        //          F (
        //              LPAREN (),
        //              E (
        //                  T (
        //                      F (N ()),
        //                      T' ()
        //                    ),
        //                  E' ()
        //                ),
        //              RPAREN ()
        //            ),
        //          T' ()
        //        ),
        //      E' ()
        //   )
        Tree tree = new Tree("E",
                new Tree("T",
                        new Tree("F",
                                new Tree("LPAREN"),
                                new Tree("E",
                                        new Tree("T",
                                                new Tree("F", new Tree("N")),
                                                new Tree("T'")
                                        ),
                                        new Tree("E'")
                                ),
                                new Tree("RPAREN")
                        ),
                        new Tree("T'")
                ),
                new Tree("E'")
        );

        test("(0)", tree);
        test("\t (   0)", tree);
        test("( 1 \t)", tree);
        test("(52) \r  ", tree);
        test("(812)\t\t", tree);
        test("(\r\r735)", tree);

    }

    @Test
    public void testBracketsWithFunction() throws ParseException {
        // test smth like: (sin 12)
        // changed inside LPAREN ... RPAREN to what was in function
        Tree tree = new Tree("E",
                new Tree("T",
                        new Tree("F",
                                new Tree("LPAREN"),
                                new Tree("E",
                                        new Tree("T",
                                                new Tree("F", new Tree("FUNC"), new Tree("F", new Tree("N"))),
                                                new Tree("T'")
                                        ),
                                        new Tree("E'")
                                ),
                                new Tree("RPAREN")
                        ),
                        new Tree("T'")
                ),
                new Tree("E'")
        );

        test("(sin 1)", tree);
        test("(cos2)", tree);
        test("(f 123)", tree);
        test("(mycoolfun 79528125252)", tree);

    }
}
