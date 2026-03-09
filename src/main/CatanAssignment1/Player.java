package CatanAssignment1;


import java.util.Random;

public abstract class Player {

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

    protected Random getRandom() {
        return random;
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

    protected boolean canAffordAnyBuild() {
        ResourceMap roadCost = BuildAction.costFor(BuildType.ROAD);
        ResourceMap settlementCost = BuildAction.costFor(BuildType.SETTLEMENT);
        ResourceMap cityCost = BuildAction.costFor(BuildType.CITY);
        return resourceHand.canAfford(roadCost) || resourceHand.canAfford(settlementCost)
                || resourceHand.canAfford(cityCost);
    }

    /**
     * Takes a turn and returns the chosen BuildAction (which may be PASS).
     */
    public abstract BuildAction takeTurn(Board board);
}
