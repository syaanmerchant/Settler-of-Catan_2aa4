package CatanAssignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandHistoryTest {

    private CommandHistory history;
    private Board board;
    private Player p;
    private int roll;
    private ResourceType res;

    @BeforeEach
    void setUp() {
        // fresh history/board/player for each test
        history = new CommandHistory();
        board = new Board();
        board.setupMap();
        p = new MachinePlayer(0);

        // find a real tile so produce commands actually move resources
        Tile t = board.getTiles().stream()
                .filter(tile -> tile.getResourceType() != ResourceType.DESERT && tile.getNumberToken() != 0)
                .findFirst()
                .orElseThrow();
        roll = t.getNumberToken();
        res = t.getResourceType();
        t.getCornerNodes().get(0).setStructureOwner(p);
        t.getCornerNodes().get(0).setStructureType(StructureType.SETTLEMENT);
    }

    // after execute, undo stack is non-empty and redo stack is empty
    @Test
    void executeCommand_canUndo_cannotRedo() {
        ProduceCommand cmd = new ProduceCommand(board, roll, List.of(p));
        history.executeCommand(cmd);
        assertTrue(history.canUndo());
        assertFalse(history.canRedo());
    }

    // after execute then undo, canRedo is true and resources were actually restored
    @Test
    void undo_afterExecute_canRedo_andUndoWasCalled() {
        int before = p.getResourceHand().getResources().get(res);
        ProduceCommand cmd = new ProduceCommand(board, roll, List.of(p));
        history.executeCommand(cmd);
        assertTrue(p.getResourceHand().getResources().get(res) > before);

        history.undo();
        assertTrue(history.canRedo());
        assertEquals(before, p.getResourceHand().getResources().get(res));
    }

    // after execute, undo, redo: canUndo is true and execute was called again
    @Test
    void redo_afterExecuteUndo_canUndo_andExecuteCalledAgain() {
        int before = p.getResourceHand().getResources().get(res);
        ProduceCommand cmd = new ProduceCommand(board, roll, List.of(p));
        history.executeCommand(cmd);
        history.undo();
        history.redo();

        assertTrue(history.canUndo());
        assertTrue(p.getResourceHand().getResources().get(res) > before);
    }

    // executing a new command after an undo should wipe the redo stack
    @Test
    void executeCommand_afterUndo_clearsRedoStack() {
        ProduceCommand cmd1 = new ProduceCommand(board, roll, List.of(p));
        ProduceCommand cmd2 = new ProduceCommand(board, roll, List.of(p));
        ProduceCommand cmd3 = new ProduceCommand(board, roll, List.of(p));

        history.executeCommand(cmd1);
        history.executeCommand(cmd2);
        history.undo();
        assertTrue(history.canRedo());

        history.executeCommand(cmd3);
        assertFalse(history.canRedo());
    }

    // undo on an empty stack should be a no-op, not a crash
    @Test
    void undo_onEmptyStack_doesNothing() {
        assertDoesNotThrow(() -> history.undo());
        assertFalse(history.canUndo());
        assertFalse(history.canRedo());
    }
}
