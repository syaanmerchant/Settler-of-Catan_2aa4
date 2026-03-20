package CatanAssignment1;

import java.util.Map;

/**
 * Immediate value rule for spending cards such that fewer than 5 remain.
 * Returns 0.5 when the action's cost would leave the agent with < 5 cards.
 */
public class LowHandAfterSpendRule implements ImmediateValueRule {

    @Override
    public double valueIfApplies(MachinePlayer agent, BuildAction action) {
        if (agent == null || action == null) {
            return 0.0;
        }
        if (action.getType() == BuildType.PASS) {
            return 0.0;
        }

        int before = agent.getResourceHand().totalCards();
        int costTotal = totalResourceCost(action.cost());
        int after = before - costTotal;

        return after < 5 ? 0.5 : 0.0;
    }

    private int totalResourceCost(ResourceMap cost) {
        if (cost == null) {
            return 0;
        }
        int sum = 0;
        for (Map.Entry<ResourceType, Integer> e : cost.asMap().entrySet()) {
            if (e.getValue() != null) {
                sum += e.getValue();
            }
        }
        return sum;
    }
}

