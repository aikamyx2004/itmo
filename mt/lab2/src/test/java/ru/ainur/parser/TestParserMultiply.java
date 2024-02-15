package ru.ainur.parser;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class TestParserMultiply extends TestParserBase {

    @Test
    public void testMultiply() throws ParseException {
        // 1 * 2
        // E (T E'), E' --- eps
        // T --- T (F, T'), F --- 1,  T' --- * 2
        // 1 --- F (N ())
        // * 2 --- T' (*, F (N ()), T' ())
        // 1 * 2 --- E (T (F (N ()), T' (*, F (N ()), T' ())), T')

        // E (
        //      T (
        //          F  (N ()),
        //          T' (
        //                  *,
        //                  F (N ()),
        //                  T' ()
        //             )
        //        ),
        //      E' ()
        //   )


        Tree tree = new Tree(
                "E",
                new Tree(
                        "T",
                        new Tree("F", new Tree("N")),
                        new Tree("T'",
                                new Tree("*"),
                                new Tree("F", new Tree("N")),
                                new Tree("T'")
                        )
                ),
                new Tree("E'")
        );
        System.out.println(parse("1 * 2"));
        test("1\r * 2", tree);
        test("-12 *\t 2", tree);
        test("52 * 812\t", tree);
        test("\t-735234  *\r -112313", tree);
    }

    @Test
    public void testInvalidMultiply() throws ParseException {
        testInvalid("\t100 *");
        testInvalid("* 100");
        testInvalid("100 * 52 * ");
        testInvalid("*");
    }
}
