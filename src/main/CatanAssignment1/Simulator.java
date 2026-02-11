package main.CatanAssignment1;

import java.util.List;

/**
 * Coordinates the Catan simulation: rounds, turns, production, and termination.
 */
public class Simulator {

    private int maxRounds;
    private int currentRound;
    private Board board;
    private List<Player> players;
    private static final int WIN_VP = 10;

    public Simulator(Board board, List<Player> players, int maxRounds) {
        this.board = board;
        this.players = players;
        this.maxRounds = Math.min(maxRounds, 8192);
        this.currentRound = 0;
    }

    /**
     * Runs the simulation until max rounds or a player reaches 10 VP.
     * Per round: each player rolls, produces (if not 7), then may build.
     * Prints actions and VPs at end of each round.
     */
    public void runSimulation() {
        while (currentRound < maxRounds) {
            for (Player p : players) {
                int roll = p.rollDice();
                System.out.println("P" + (p.getId() + 1) + " ROLL " + roll);

                if (roll != 7) {
                    board.produce(roll);
                }

                BuildAction action = p.takeTurn(board);
                System.out.println("P" + (p.getId() + 1) + " " + action.describe());
            }

            currentRound++;
            printVictoryPoints();

            if (checkWin()) {
                break;
            }
        }
    }

    private void printVictoryPoints() {
        StringBuilder sb = new StringBuilder("Round " + currentRound + ": ");
        for (int i = 0; i < players.size(); i++) {
            if (i > 0)
                sb.append(" ");
            sb.append("VP").append(i + 1).append("=").append(players.get(i).getVictoryPoints());
        }
        System.out.println(sb.toString());
    }

    public boolean checkWin() {
        for (Player p : players) {
            if (p.getVictoryPoints() >= WIN_VP) {
                return true;
            }
        }
        return false;
    }

    public int getCurrentRound() {
        return currentRound;
    }
}
