package CatanAssignment1;

/**
 * Wraps a single build action so it can be undone.
 * Snapshots the player/board state before executing.
 */
public class BuildCommand implements GameCommand {

    private final Board board;
    private final Player player;
    private final BuildAction action;
    private final int vpBefore;
    private final ResourceMap handBefore;
    private final Player prevEdgeOwner;
    private final Player prevNodeOwner;
    private final StructureType prevNodeStructure;

    public BuildCommand(Board board, Player player, BuildAction action) {
        this.board = board;
        this.player = player;
        this.action = action;

        this.vpBefore = player.getVictoryPoints();

        // deep copy of the player's hand so undo can restore it
        this.handBefore = new ResourceMap();
        ResourceMap live = player.getResourceHand().getResources();
        for (ResourceType rt : ResourceType.values()) {
            if (rt == ResourceType.DESERT)
                continue;
            handBefore.put(rt, live.get(rt));
        }

        if (action.getType() == BuildType.ROAD) {
            this.prevEdgeOwner = action.getEdge().getRoadOwner();
            this.prevNodeOwner = null;
            this.prevNodeStructure = null;
        } else {
            this.prevEdgeOwner = null;
            if (action.getNode() != null) {
                this.prevNodeOwner = action.getNode().getStructureOwner();
                this.prevNodeStructure = action.getNode().getStructureType();
            } else {
                this.prevNodeOwner = null;
                this.prevNodeStructure = null;
            }
        }
    }

    @Override
    public void execute() {
        board.executeBuild(player, action);
    }

    @Override
    public void undo() {
        int vpDelta = vpBefore - player.getVictoryPoints();
        if (vpDelta != 0) {
            player.addVictoryPoints(vpDelta);
        }

        ResourceMap live = player.getResourceHand().getResources();
        for (ResourceType rt : ResourceType.values()) {
            if (rt == ResourceType.DESERT)
                continue;
            live.put(rt, handBefore.get(rt));
        }

        switch (action.getType()) {
            case ROAD:
                action.getEdge().setRoadOwner(prevEdgeOwner);
                break;
            case SETTLEMENT:
            case CITY:
                action.getNode().setStructureOwner(prevNodeOwner);
                action.getNode().setStructureType(prevNodeStructure);
                break;
            default:
                break;
        }
    }

    @Override
    public String describe() {
        return action.describe() + " by P" + (player.getId() + 1);
    }
}
