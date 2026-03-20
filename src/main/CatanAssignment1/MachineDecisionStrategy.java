package CatanAssignment1;

import java.util.List;

/**
 * Strategy pattern: encapsulates the decision policy for picking a single action
 * from a list of valid candidate actions.
 */
public interface MachineDecisionStrategy {

    BuildAction chooseAction(MachinePlayer agent, Board board, List<BuildAction> candidates);
}

