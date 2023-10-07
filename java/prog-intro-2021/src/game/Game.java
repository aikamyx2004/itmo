package game;

import java.util.List;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("It is game tic tac toe on hex board");
        System.out.println("Players will play tournament\n");
        System.out.println("Please write game parameters");
        System.out.println("Write them in one line and split by whitespaces\n");
        System.out.println("Write n, m and k");
        int n, m, k;
        while (true) {
            List<Integer> tempIntegers = Reader.readIntegers(3, in, 1);
            n = tempIntegers.get(0);
            m = tempIntegers.get(1);
            k = tempIntegers.get(2);
            if (k <= Math.max(n, m)) {
                break;
            }
            System.out.println("Wrong k, it is more than n and m, please write again");
        }
        System.out.println("\nWrite count of players");
        int countOfPlayers = Reader.readIntegers(1, in, 2).get(0);

        System.out.println("\nWrite type of each player");
        System.out.println("Use h for human player, s - sequential player, r - randomPlayer");
        Player[] players = new Player[countOfPlayers];
        List<String> types = readPlayers(countOfPlayers, players, in);

        System.out.println("\nIf you want to know information about all moves write 'yes'");
        boolean log = "yes".equals(in.nextLine().strip());

        Tournament tournament = new Tournament(players, n, m, k);
        int[][] leaderboard = tournament.play(log);
        System.out.println("Result of tournament:");
        for (int i = 0; i < countOfPlayers; i++) {
            System.out.printf("Player #%d, is a %s, has score: %d\n", leaderboard[i][1],
                    switch (types.get(i)) {
                        case "h" -> "HumanPlayer";
                        case "s" -> "SequentialPlayer";
                        default -> "RandomPlayer";
                    }, leaderboard[i][0]);
        }

    }

    private static List<String> readPlayers(int count, Player[] players, Scanner in) {
        outer:
        while (true) {
            List<String> types = Reader.readTokens(count, in, String::toString);
            for (int i = 0; i < count; i++) {
                String type = types.get(i);
                if (!type.equals("h") && !type.equals("s") && !type.equals("r")) {
                    System.out.printf("Wrong format, I do not know player %s\n", type);
                    continue outer;
                }
            }
            for (int i = 0; i < count; i++) {
                players[i] = switch (types.get(i)) {
                    case ("h") -> new HumanPlayer(in);
                    case ("s") -> new SequentialPlayer();
                    default -> new RandomPlayer();
                };
            }
            return types;
        }
    }


}
