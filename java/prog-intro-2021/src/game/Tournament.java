package game;

import java.util.Arrays;
import java.util.Random;

public class Tournament {
    private final Player[] players;
    private final int n, m, k;

    public Tournament(Player[] players, int n, int m, int k) {
        this.players = players;
        this.n = n;
        this.m = m;
        this.k = k;
    }

    public int[][] play(boolean log) {
        Random random = new Random();
        int[] score = new int[players.length];
        for (int i = 1; i < players.length; i++) {
            for (int j = 0; j < i; j++) {
                int first = i;
                int second = j;
                if (random.nextBoolean()) {
                    first = j;
                    second = i;
                }
                twoPlayerGame(score, first, second, log);
            }
        }
        return sortScore(score);
    }

    private void twoPlayerGame(int[] score, int first, int second, boolean log) {
        final int result = new TwoPlayerGame(
                new HexBoard(n, m, k),
                players[first],
                players[second]
        ).play(log);
        System.out.printf("Game between players %d and %d\n", first + 1, second + 1);

        switch (result) {
            case 1 -> {
                System.out.printf("Player #%d won\n", first + 1);
                score[first] += 3;
            }
            case 2 -> {
                System.out.printf("Player #%d won\n", second + 1);
                score[second] += 3;
            }
            case 0 -> {
                System.out.println("Draw");
                score[first]++;
                score[second]++;
            }
            default -> throw new AssertionError("Unknown result " + result);
        }
        System.out.println();
    }

    private int[][] sortScore(int[] score) {
        int[][] leaderboard = new int[players.length][2];
        for (int i = 0; i < players.length; i++) {
            leaderboard[i][0] = score[i];
            leaderboard[i][1] = i + 1;
        }
        Arrays.sort(leaderboard, (o1, o2) -> {
            if (o1[0] == o2[0])
                return -Integer.compare(o1[1], o2[1]);
            return -Integer.compare(o1[0], o2[0]);
        });
        return leaderboard;
    }
}
