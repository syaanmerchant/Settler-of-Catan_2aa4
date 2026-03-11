package CatanAssignment1;


import java.util.List;
import java.util.Random;

/**
 * Coordinates the Catan simulation: rounds, turns, production, and termination.
 */
public class Simulator {

    public interface HumanTurnContext {
        void applyRoll(HumanPlayer human, int roll);
    }

    private int maxRounds;
    private int currentRound;
    private Board board;
    private List<Player> players;
    private final GameStateWriter gameStateWriter;
    private static final int WIN_VP = 10;
    private final Random random = new Random();

    public Simulator(Board board, List<Player> players, int maxRounds, GameStateWriter gameStateWriter) {
        this.board = board;
        this.players = players;
        this.maxRounds = Math.min(maxRounds, 8192);
        this.currentRound = 0;
        this.gameStateWriter = gameStateWriter;
    }

    /**
     * Runs the simulation until max rounds or a player reaches 10 VP.
     * Per round: each player rolls, produces (if not 7), then may build.
     * Prints actions and VPs at end of each round.
     */
    public void runSimulation() {
        HumanPlayer human = findHumanPlayer();
        while (currentRound < maxRounds) {
            int roundNumber = currentRound + 1;

            for (Player p : players) {
                if (p instanceof HumanPlayer) {
                    human.playTurn(roundNumber, board, (h, roll) -> applyRollForPlayer(h, roll));
                } else {
                    int roll = p.rollDice();
                    applyRollForPlayer(p, roll);
                    BuildAction action = p.takeTurn(board);
                    System.out.println(roundNumber + " / P" + (p.getId() + 1) + ": " + action.describe());
                }

                if (gameStateWriter != null) {
                    gameStateWriter.write(board, players);
                }

                // Step-forward: after any non-human agent acts, wait for GO.
                if (human != null && !(p instanceof HumanPlayer)) {
                    human.waitForGoGate(roundNumber);
                }
            }

            currentRound++;
            printVictoryPoints();

            if (checkWin()) {
                break;
            }
        }
    }

    private void applyRollForPlayer(Player roller, int roll) {
        if (roll != 7) {
            board.produce(roll);
            return;
        }
        board.handleRobberRoll(roller, players, random);
    }

    private HumanPlayer findHumanPlayer() {
        for (Player p : players) {
            if (p instanceof HumanPlayer) {
                return (HumanPlayer) p;
            }
        }
        return null;
    }


    private void printVictoryPoints() {
        int roundNumber = currentRound; 
        StringBuilder sb = new StringBuilder(roundNumber + " / VP:");
        for (int i = 0; i < players.size(); i++) {
            sb.append(" P").append(i + 1).append("=").append(players.get(i).getVictoryPoints());
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
