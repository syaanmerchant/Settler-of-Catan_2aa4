package CatanAssignment1;

/**
 * Represents a single reversible game action (Command pattern).
 * Concrete implementations (e.g. BuildCommand, ProduceCommand) store
 * enough state to both apply and undo the action.
 */
public interface GameCommand {

    /** Applies this action to the game state. */
    void execute();

    /** Reverts the game state to before this action was executed. */
    void undo();

    /** Short description for console output, e.g. "ROAD E5". */
    String describe();
}
