package game;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner in;

    public HumanPlayer(Scanner in) {
        this.in = in;
    }

    @Override
    public Move makeMove(Position position) {
        System.out.println();
        System.out.println("Current position");
        System.out.println(position);
        System.out.println("Enter you move for " + position.getTurn());
        System.out.println("Write your move: 'row column'");
        while (true) {
            List<Integer> tempIntegers = Reader.readIntegers(2, in, 1);
            int row = tempIntegers.get(0) - 1;
            int column = tempIntegers.get(1) - 1;
            if (!position.isValid(row, column)) {
                System.out.println("Wrong format, write again");
                continue;
            }
            return new Move(row, column, position.getTurn());
        }
    }
}
