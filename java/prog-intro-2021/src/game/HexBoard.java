package game;

import java.util.Arrays;
import java.util.Map;

public class HexBoard implements Board, Position {
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0"
    );
    private final int n, m, k;
    private int countFilled = 0;
    private final Cell[][] field;
    private Cell turn;
    private final Position position;

    public HexBoard(int n, int m, int k) {
        this.n = n;
        this.m = m;
        this.k = k;
        field = new Cell[n][m];
        for (Cell[] row : field) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
        position = new BoardPosition(this);
    }


    @Override
    public Cell getTurn() {
        return turn;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public GameResult makeMove(Move move) {
        if (!isValid(move)) {
            return GameResult.LOOSE;
        }
        field[move.getRow()][move.getCol()] = move.getValue();
        countFilled++;
        if (checkWin(move)) {
            return GameResult.WIN;
        }

        if (checkDraw()) {
            return GameResult.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return GameResult.UNKNOWN;
    }

    private boolean checkDraw() {
        return countFilled == n * m;
    }

    private boolean checkWin(Move move) {
        int row = move.getRow();
        int col = move.getCol();
        int[][] direction = new int[][]{{0, 1}, {1, 0}, {1, 1}};
        for (int type = 0; type < 3; type++) {
            int count = 0;
            for (int j = 0; ; j++, count++) {
                if (isBadCell(row, col, j, direction[type])) {
                    break;
                }
            }
            for (int j = -1; ; j--, count++) {
                if (isBadCell(row, col, j, direction[type])) {
                    break;
                }
            }
            if (count >= k) {
                return true;
            }
        }
        return false;
    }

    private boolean isBadCell(int row, int col, int j, int[] direction) {
        int newRow = row + j * direction[0];
        int newCol = col + j * direction[1];
        return !isValid(newRow, newCol) || getCell(newRow, newCol) != turn;
    }

    @Override
    public int getRow() {
        return n;
    }

    @Override
    public int getCol() {
        return m;
    }

    @Override
    public boolean isValid(int row, int col) {
        return 0 <= row && row < n
                && 0 <= col && col < m;
    }

    public boolean isValid(final Move move) {
        return isValid(move.getRow(), move.getCol())
                && field[move.getRow()][move.getCol()] == Cell.E
                && turn == move.getValue();
    }

    @Override
    public Cell getCell(int row, int column) {
        return field[row][column];
    }

    private int length(int number) {
        int count = 0;
        while (number > 0) {
            count++;
            number /= 10;
        }
        return count;
    }

    private String whitespaces(int count) {
        return " ".repeat(count);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("  ");
        int len = length(m);
        for (int j = 0; j < m; j++) {
            sb.append(j + 1).append(whitespaces(len + 1 - length(j + 1)));
        }
        sb.append(System.lineSeparator());
        for (int i = 0; i < n; i++) {
            sb.append(whitespaces(i + 1 - length(i + 1)));
            sb.append(i + 1).append(' ');
            for (int j = 0; j < m; j++) {
                sb.append(CELL_TO_STRING.get(field[i][j])).append(whitespaces(len));
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
