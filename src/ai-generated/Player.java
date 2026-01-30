package CatanAssignment1;

import java.util.Random;

public class Player {

    // UML: - id: Integer [1]
    private Integer id;

    // UML: - victoryPoints: Integer [1]
    private Integer victoryPoints;

    // UML association role: + simulator (1)
    private Simulator simulator;

    // UML composition role: + resourcehand (1)
    private ResourceHand resourcehand = new ResourceHand();

    // UML: + rollDice(): Integer
    public Integer rollDice() {
        // Simple 2d6 roll (placeholder)
        Random r = new Random();
        return (r.nextInt(6) + 1) + (r.nextInt(6) + 1);
    }

    // UML: + takeTurn( in board: Board)
    public void takeTurn(Board board) {
        // Placeholder turn logic
        Integer roll = rollDice();
        if (board != null) {
            board.produce(roll);
        }
    }
}
