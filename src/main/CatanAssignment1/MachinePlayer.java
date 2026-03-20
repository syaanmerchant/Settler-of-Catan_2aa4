package CatanAssignment1;

import java.util.ArrayList;
import java.util.List;

/**
 * Automated player that selects a random valid build when possible.
 */
public class MachinePlayer extends Player {

    private MachineDecisionStrategy strategy;

    public MachinePlayer(int id) {
        super(id);
        this.strategy = createDefaultStrategy();
    }

    private static MachineDecisionStrategy createDefaultStrategy() {
        List<ImmediateValueRule> rules = new ArrayList<>();
        // Immediate values as required by Task 2 (R3.2).
        rules.add(new VpEarningRule());
        rules.add(new NonVpBuildRule());
        rules.add(new LowHandAfterSpendRule());
        return new ImmediateValueDecisionStrategy(rules);
    }

    public void setStrategy(MachineDecisionStrategy strategy) {
        if (strategy != null) {
            this.strategy = strategy;
        }
    }

    /**
     * Takes a turn: optionally builds (if >7 cards or can afford a valid build).
     * Returns the BuildAction taken (for printing).
     */
    @Override
    public BuildAction takeTurn(Board board) {
        List<BuildAction> validBuilds = new ArrayList<>();

        if (getResourceHand().totalCards() > 7 || canAffordAnyBuild()) {
            for (Edge e : board.getEdges()) {
                if (e.isEmpty() && getResourceHand().canAfford(BuildAction.costFor(BuildType.ROAD))) {
                    BuildAction a = new BuildAction(BuildType.ROAD, null, e);
                    if (board.validateBuild(this, a)) {
                        validBuilds.add(a);
                    }
                }
            }
            for (Node n : board.getNodes()) {
                if (n.isEmpty() && getResourceHand().canAfford(BuildAction.costFor(BuildType.SETTLEMENT))) {
                    BuildAction a = new BuildAction(BuildType.SETTLEMENT, n, null);
                    if (board.validateBuild(this, a)) {
                        validBuilds.add(a);
                    }
                }
                if (n.getStructureOwner() == this && n.getStructureType() == StructureType.SETTLEMENT
                        && getResourceHand().canAfford(BuildAction.costFor(BuildType.CITY))) {
                    BuildAction a = new BuildAction(BuildType.CITY, n, null);
                    if (board.validateBuild(this, a)) {
                        validBuilds.add(a);
                    }
                }
            }
        }

        if (validBuilds.isEmpty()) {
            return new BuildAction(BuildType.PASS, null, null);
        }

        BuildAction chosen = strategy.chooseAction(this, board, validBuilds);
        if (chosen == null) {
            chosen = validBuilds.get(getRandom().nextInt(validBuilds.size()));
        }
        board.executeBuild(this, chosen);
        return chosen;
    }
}

