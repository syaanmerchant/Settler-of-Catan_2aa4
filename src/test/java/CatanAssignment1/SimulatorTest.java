package CatanAssignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatorTest {

    private Board board;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.setupMap();
        players = new ArrayList<>();
        players.add(new MachinePlayer(0));
    }

    // fresh simulator should expose a non-null CommandHistory
    @Test
    void simulator_history_isNotNull_onConstruction() {
        Simulator sim = new Simulator(board, players, 1, null);
        assertNotNull(sim.getHistory());
    }

    // after running rounds, at least one ProduceCommand should be in the history
    // (only fails if every single dice roll is 7, probability ≈ 1.6e-8 for 10 rounds)
    @Test
    void simulator_usesCommandHistory_afterRoll() {
        Simulator sim = new Simulator(board, players, 10, null);
        sim.runSimulation();
        assertTrue(sim.getHistory().canUndo());
    }
}
