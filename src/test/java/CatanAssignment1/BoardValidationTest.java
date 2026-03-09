package CatanAssignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardValidationTest {
    private Board board;
    private Player p0;

    @BeforeEach
    void setUp() {
        // Fresh board/player for each test to avoid errors
        board = new Board();
        board.setupMap();
        p0 = new MachinePlayer(0);
    }

    // Invariant: cannot place a road on an already-occupied edge
    @Test
    void validateRoadBuild_fails_whenEdgeAlreadyOwned() {
        Edge e = board.getEdges().get(0);
        e.setRoadOwner(p0);

        BuildAction a = new BuildAction(BuildType.ROAD, null, e);
        assertFalse(board.validateBuild(p0, a), "Should not allow road on an occupied edge");
    }

    // Invariant: cannot place a settlement on an already-occupied node
    @Test
    void validateSettlementBuild_fails_whenNodeAlreadyOccupied() {
        Node n = board.getNodes().get(0);
        n.setStructureOwner(p0);
        n.setStructureType(StructureType.SETTLEMENT);

        BuildAction a = new BuildAction(BuildType.SETTLEMENT, n, null);
        assertFalse(board.validateBuild(p0, a), "Should not allow settlement on occupied node");
    }

    // Distance rule: settlements must be at least 2 intersections apart (no adjacent structures)
    @Test
    void validateSettlementBuild_fails_whenDistanceRuleViolated() {
        // Put an existing settlement at node 0
        Node n0 = board.getNodes().get(0);
        n0.setStructureOwner(p0);
        n0.setStructureType(StructureType.SETTLEMENT);

        // Try to build at a neighbor of node 0 (should be invalid)
        Node neighbor = n0.getNeighbourNodes().get(0);

        // Ensure connectivity doesn't accidentally block the test: give the player a road incident to the target node
        Edge inc = neighbor.getIncidentEdges().get(0);
        inc.setRoadOwner(p0);

        BuildAction a = new BuildAction(BuildType.SETTLEMENT, neighbor, null);
        assertFalse(board.validateBuild(p0, a), "Should fail when adjacent node has a structure (distance rule)");
    }

    // City upgrade rule: only allowed when upgrading your own settlement
    @Test
    void validateCityBuild_onlyAllowed_onPlayersExistingSettlement() {
        Node n = board.getNodes().get(1);

        // No settlement yet
        BuildAction a1 = new BuildAction(BuildType.CITY, n, null);
        assertFalse(board.validateBuild(p0, a1), "City requires an existing settlement owned by player");

    // Correct case: place a settlement, then upgrade it.
        n.setStructureOwner(p0);
        n.setStructureType(StructureType.SETTLEMENT);

        BuildAction a2 = new BuildAction(BuildType.CITY, n, null);
        assertTrue(board.validateBuild(p0, a2), "Should allow city upgrade on player's settlement");
    }
}
