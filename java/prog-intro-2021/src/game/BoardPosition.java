package game;

public class BoardPosition implements Position {
    private final HexBoard board;

    public BoardPosition(HexBoard board) {
        this.board = board;
    }

    @Override
    public Cell getTurn() {
        return board.getTurn();
    }


    @Override
    public int getRow() {
        return board.getRow();
    }

    @Override
    public int getCol() {
        return board.getCol();
    }

    @Override
    public boolean isValid(int row, int col) {
        return board.isValid(row, col);
    }

    @Override
    public boolean isValid(Move move) {
        return board.isValid(move);
    }

    @Override
    public Cell getCell(int row, int column) {
        return board.getCell(row, column);
    }

    @Override
    public String toString() {
        return board.toString();
    }
}