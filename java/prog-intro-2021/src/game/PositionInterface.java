package game;

public interface PositionInterface {
    Cell getTurn();

    boolean isValid(Move move);

    Cell getCell(int row, int column);
}
