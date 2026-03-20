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

    // Road: edge should be claimed and resources deducted after a valid executeBuild
    @Test
    void executeBuild_road_deductsResourcesAndPlacesRoad() {
        // give player a settlement at node 0 so they're connected to edge 0
        Node n = board.getNodes().get(0);
        n.setStructureOwner(p0);
        n.setStructureType(StructureType.SETTLEMENT);

        Edge e = board.getEdges().get(0);
        p0.getResourceHand().addResource(ResourceType.WOOD, 1);
        p0.getResourceHand().addResource(ResourceType.BRICK, 1);

        BuildAction a = new BuildAction(BuildType.ROAD, null, e);
        board.executeBuild(p0, a);

        assertEquals(p0, e.getRoadOwner());
        assertEquals(0, p0.getResourceHand().getResources().get(ResourceType.WOOD));
    }

    // Settlement: VP goes up by 1 and the node reflects the new structure
    @Test
    void executeBuild_settlement_awardsVP() {
        // connect player to node 0 via a road on edge 0
        board.getEdges().get(0).setRoadOwner(p0);
        Node n = board.getNodes().get(0);

        p0.getResourceHand().addResource(ResourceType.WOOD, 1);
        p0.getResourceHand().addResource(ResourceType.BRICK, 1);
        p0.getResourceHand().addResource(ResourceType.WHEAT, 1);
        p0.getResourceHand().addResource(ResourceType.SHEEP, 1);

        BuildAction a = new BuildAction(BuildType.SETTLEMENT, n, null);
        board.executeBuild(p0, a);

        assertEquals(1, p0.getVictoryPoints());
        assertEquals(p0, n.getStructureOwner());
        assertEquals(StructureType.SETTLEMENT, n.getStructureType());
    }

    // With an empty hand, executeBuild should silently skip — edge stays unowned
    @Test
    void executeBuild_insufficientResources_skips() {
        // valid road position but no resources
        Node n = board.getNodes().get(0);
        n.setStructureOwner(p0);
        n.setStructureType(StructureType.SETTLEMENT);

        Edge e = board.getEdges().get(0);

        BuildAction a = new BuildAction(BuildType.ROAD, null, e);
        board.executeBuild(p0, a);

        assertNull(e.getRoadOwner());
        assertEquals(0, p0.getVictoryPoints());
    }
}
