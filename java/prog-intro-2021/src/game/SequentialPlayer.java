package game;

public class SequentialPlayer implements Player {
    @Override
    public Move makeMove(Position position) {
        for (int r = 0; r < position.getRow(); r++) {
            for (int c = 0; c < position.getCol(); c++) {
                final Move move = new Move(r, c, position.getTurn());
                if (position.isValid(move)) {
                    return move;
                }
            }
        }
        throw new AssertionError("No valid moves");
    }
}
