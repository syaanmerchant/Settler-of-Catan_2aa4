package CatanAssignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
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

    // applyRollForPlayer routes non-7 rolls through CommandHistory → undo stack grows
    @Test
    void applyRollForPlayer_pushesToCommandHistory() {
        Simulator sim = new Simulator(board, players, 5, null);
        sim.runSimulation();
        assertTrue(sim.getHistory().getUndoStackSize() > 0);
    }

    // simulation should stop exactly at maxRounds when no one wins
    @Test
    void runSimulation_stopsAtMaxRounds() {
        // 4 players with empty hands → no builds → no VP → no early exit
        List<Player> four = new ArrayList<>();
        for (int i = 0; i < 4; i++) four.add(new MachinePlayer(i));
        Simulator sim = new Simulator(board, four, 2, null);
        sim.runSimulation();
        assertEquals(2, sim.getCurrentRound());
    }

    // player at 10 VP → checkWin triggers, sim ends after first round
    @Test
    void checkWin_returnsTrue_whenPlayerHas10VP() {
        players.get(0).addVictoryPoints(10);
        Simulator sim = new Simulator(board, players, 5, null);
        sim.runSimulation();
        assertEquals(1, sim.getCurrentRound());
    }

    // non-null writer should produce a file on disk
    @Test
    void simulator_passesNonNullWriter_writesState() {
        File tmp = new File("test_state_" + System.nanoTime() + ".json");
        tmp.deleteOnExit();
        GameStateWriter writer = new GameStateWriter(tmp.getAbsolutePath());

        Simulator sim = new Simulator(board, players, 1, writer);
        sim.runSimulation();

        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 0);
    }
}
