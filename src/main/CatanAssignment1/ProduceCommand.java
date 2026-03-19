package CatanAssignment1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wraps a single resource-production step (dice roll) so it can be undone.
 * Snapshots every player's hand before producing.
 */
public class ProduceCommand implements GameCommand {

    private final Board board;
    private final int roll;
    private final List<Player> players;
    private final Map<Player, ResourceMap> handSnapshots = new HashMap<>();

    public ProduceCommand(Board board, int roll, List<Player> players) {
        this.board = board;
        this.roll = roll;
        this.players = players;

        for (Player p : players) {
            ResourceMap copy = new ResourceMap();
            ResourceMap live = p.getResourceHand().getResources();
            for (ResourceType rt : ResourceType.values()) {
                if (rt == ResourceType.DESERT)
                    continue;
                copy.put(rt, live.get(rt));
            }
            handSnapshots.put(p, copy);
        }
    }

    @Override
    public void execute() {
        board.produce(roll);
    }

    @Override
    public void undo() {
        for (Player p : players) {
            ResourceMap snapshot = handSnapshots.get(p);
            ResourceMap live = p.getResourceHand().getResources();
            for (ResourceType rt : ResourceType.values()) {
                if (rt == ResourceType.DESERT)
                    continue;
                live.put(rt, snapshot.get(rt));
            }
        }
    }

    @Override
    public String describe() {
        return "Produce on roll: " + roll;
    }
}
