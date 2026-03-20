package CatanAssignment1;

import java.util.ArrayList;
import java.util.List;

/**
 * Chooses the candidate action with the highest immediate value.
 *
 * Value computation:
 * - Each rule may apply to the action.
 * - The score of an action is the maximum value among applicable rules.
 * - If multiple actions tie for the best score, break ties randomly.
 */
public class ImmediateValueDecisionStrategy implements MachineDecisionStrategy {

    private final List<ImmediateValueRule> rules;

    public ImmediateValueDecisionStrategy(List<ImmediateValueRule> rules) {
        this.rules = (rules != null) ? rules : new ArrayList<>();
    }

    @Override
    public BuildAction chooseAction(MachinePlayer agent, Board board, List<BuildAction> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return new BuildAction(BuildType.PASS, null, null);
        }

        double bestScore = Double.NEGATIVE_INFINITY;
        List<BuildAction> bestActions = new ArrayList<>();

        for (BuildAction action : candidates) {
            double score = 0.0;
            for (ImmediateValueRule rule : rules) {
                double v = rule.valueIfApplies(agent, action);
                if (v > score) {
                    score = v;
                }
            }

            int cmp = Double.compare(score, bestScore);
            if (cmp > 0) {
                bestScore = score;
                bestActions.clear();
                bestActions.add(action);
            } else if (cmp == 0) {
                bestActions.add(action);
            }
        }

        return bestActions.get(agent.getRandom().nextInt(bestActions.size()));
    }
}

