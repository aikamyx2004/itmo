package game;

public interface Position {
    int getRow();

    int getCol();

    boolean isValid(int row, int col);

    boolean isValid(Move move);

    Cell getTurn();

    Cell getCell(int row, int column);
}
