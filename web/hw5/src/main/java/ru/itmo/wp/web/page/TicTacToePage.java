package ru.itmo.wp.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    public static class State {
        private final Integer size = 3;
        private Integer marked = 0;
        private final String[][] cells = new String[size][size];
        private String phase = "RUNNING";
        private boolean crossesMove = true;


        public Integer getSize() {
            return size;
        }

        public Integer getMarked() {
            return marked;
        }

        public String[][] getCells() {
            return cells;
        }

        public String getPhase() {
            return phase;
        }

        public boolean isCrossesMove() {
            return crossesMove;
        }

        public void move(int row, int col) {
            if (state.cells[row][col] != null) {
                return;
            }
            state.cells[row][col] = getMove();
            marked++;
            if (won(getMove())) {
                phase = "WON_" + getMove();
            } else if (draw()) {
                phase = "DRAW";
            }
            crossesMove = !crossesMove;
        }

        private boolean draw() {
            return !phase.equals("WON_X") && !phase.equals("WON_O")
                    && (phase.equals("DRAW") || phase.equals("RUNNING") && marked == size * size);
        }

        private boolean won(String move) {
            return checkWin(0, 1, move) || checkWin(1, 0, move) || checkWin(1, 1, move) || checkWin(-1, 1, move);
        }

        private boolean checkWin(int dx, int dy, String move) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int x = i;
                    int y = j;
                    int cnt = 0;
                    while (Integer.min(x, y) >= 0 && Integer.max(x, y) < size && move.equals(cells[x][y])) {
                        x += dx;
                        y += dy;
                        cnt++;
                    }
                    if (cnt == size) {
                        return true;
                    }
                }
            }
            return false;
        }

        private String getMove() {
            return crossesMove ? "X" : "O";
        }
    }

    private static State state = new State();

    private void action(Map<String, Object> view) {
        newGame(view);
    }

    private void newGame(Map<String, Object> view) {
        state = new State();
        view.put("state", state);
    }

    private void onMove(HttpServletRequest request, Map<String, Object> view) {
        view.put("state", state);
        if (!state.getPhase().equals("RUNNING")) {
            return;
        }
        for (int i = 0; i < state.getSize(); i++) {
            for (int j = 0; j < state.getSize(); j++) {
                if (request.getParameter("cell_" + i + j) != null) {
                    state.move(i, j);
                    return;
                }
            }
        }
    }
}
