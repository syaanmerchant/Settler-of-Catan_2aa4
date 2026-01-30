package CatanAssignment1;

import java.util.ArrayList;
import java.util.List;

public class Simulator {

    // UML: - maxRounds: Integer [1]
    private Integer maxRounds;

    // UML: - currentRound: Integer [1]
    private Integer currentRound;

    // UML association roles: + board (1), + players (4)
    private Board board;
    private List<Player> players = new ArrayList<>();

    // UML: + runSimulation()
    public void runSimulation() {
        if (board == null) return;

        while (currentRound != null && maxRounds != null && currentRound < maxRounds) {
            for (Player p : players) {
                if (p != null) {
                    p.takeTurn(board);
                }
            }
            currentRound = currentRound + 1;
            if (Boolean.TRUE.equals(checkWin())) {
                break;
            }
        }
    }

    // UML: + checkWin(): Boolean
    public Boolean checkWin() {
        // Placeholder: no winner logic
        return Boolean.FALSE;
    }
}
