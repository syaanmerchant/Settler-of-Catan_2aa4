package main.CatanAssignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    private int id;
    private int victoryPoints;
    private ResourceHand resourceHand = new ResourceHand();
    private Random random = new Random();

    public Player(int id) {
        this.id = id;
        this.victoryPoints = 0;
    }

    public int getId() {
        return id;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public ResourceHand getResourceHand() {
        return resourceHand;
    }

    public void addVictoryPoints(int delta) {
        this.victoryPoints += delta;
    }

    /** Rolls two 6-sided dice, returns sum 2-12. */
    public int rollDice() {
        return random.nextInt(6) + 1 + random.nextInt(6) + 1;
    }

    /**
     * Takes a turn: optionally builds (if >7 cards or can afford a valid build).
     * Returns the BuildAction taken (for printing).
     */
    public BuildAction takeTurn(Board board) {
        List<BuildAction> validBuilds = new ArrayList<>();

        if (resourceHand.totalCards() > 7 || canAffordAnyBuild(board)) {
            // Collect all valid build options
            for (Edge e : board.getEdges()) {
                if (e.isEmpty() && resourceHand.canAfford(BuildAction.costFor(BuildType.ROAD))) {
                    BuildAction a = new BuildAction(BuildType.ROAD, null, e);
                    if (board.validateBuild(this, a))
                        validBuilds.add(a);
                }
            }
            for (Node n : board.getNodes()) {
                if (n.isEmpty() && resourceHand.canAfford(BuildAction.costFor(BuildType.SETTLEMENT))) {
                    BuildAction a = new BuildAction(BuildType.SETTLEMENT, n, null);
                    if (board.validateBuild(this, a))
                        validBuilds.add(a);
                }
                if (n.getStructureOwner() == this && n.getStructureType() == StructureType.SETTLEMENT
                        && resourceHand.canAfford(BuildAction.costFor(BuildType.CITY))) {
                    BuildAction a = new BuildAction(BuildType.CITY, n, null);
                    if (board.validateBuild(this, a))
                        validBuilds.add(a);
                }
            }
        }

        if (validBuilds.isEmpty()) {
            return new BuildAction(BuildType.PASS, null, null);
        }

        BuildAction chosen = validBuilds.get(random.nextInt(validBuilds.size()));
        board.executeBuild(this, chosen);
        return chosen;
    }

    private boolean canAffordAnyBuild(Board board) {
        ResourceMap roadCost = BuildAction.costFor(BuildType.ROAD);
        ResourceMap settlementCost = BuildAction.costFor(BuildType.SETTLEMENT);
        ResourceMap cityCost = BuildAction.costFor(BuildType.CITY);
        return resourceHand.canAfford(roadCost) || resourceHand.canAfford(settlementCost)
                || resourceHand.canAfford(cityCost);
    }
}
