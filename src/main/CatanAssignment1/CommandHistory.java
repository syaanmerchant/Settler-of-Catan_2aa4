package CatanAssignment1;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Manages undo/redo stacks for game commands.
 * All game actions are routed through here so they can be reversed.
 */
public class CommandHistory {

    private final Deque<GameCommand> undoStack = new ArrayDeque<>();
    private final Deque<GameCommand> redoStack = new ArrayDeque<>();

    public void executeCommand(GameCommand cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();
        System.out.println("[ACTION] " + cmd.describe());
    }

    public boolean undo() {
        if (undoStack.isEmpty()) {
            System.out.println("[UNDO] Nothing to undo.");
            return false;
        }
        GameCommand cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);
        System.out.println("[UNDO] " + cmd.describe());
        return true;
    }

    public boolean redo() {
        if (redoStack.isEmpty()) {
            System.out.println("[REDO] Nothing to redo.");
            return false;
        }
        GameCommand cmd = redoStack.pop();
        cmd.execute();
        undoStack.push(cmd);
        System.out.println("[REDO] " + cmd.describe());
        return true;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public int getUndoStackSize() {
        return undoStack.size();
    }

    public int getRedoStackSize() {
        return redoStack.size();
    }
}
