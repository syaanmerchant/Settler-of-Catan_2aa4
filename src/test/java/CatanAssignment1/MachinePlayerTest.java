package CatanAssignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MachinePlayerTest {

    private Board board;
    private MachinePlayer p;

    @BeforeEach
    void setUp() {
        // Fresh board/player for each test
        board = new Board();
        board.setupMap();
        p = new MachinePlayer(0);
    }

    // empty hand + no roads/settlements → nothing to build
    @Test
    void takeTurn_returnsPass_whenNoCandidates() {
        BuildAction action = p.takeTurn(board);
        assertEquals(BuildType.PASS, action.getType());
    }

    // WOOD+BRICK + settlement at node 0 for connectivity → road should be placed
    @Test
    void takeTurn_buildsRoad_whenAffordable() {
        Node n = board.getNodes().get(0);
        n.setStructureOwner(p);
        n.setStructureType(StructureType.SETTLEMENT);

        p.getResourceHand().addResource(ResourceType.WOOD, 1);
        p.getResourceHand().addResource(ResourceType.BRICK, 1);

        BuildAction action = p.takeTurn(board);
        assertEquals(BuildType.ROAD, action.getType());
        assertEquals(p, action.getEdge().getRoadOwner());
    }

    // default strategy scores settlement 1.0 vs road 0.8 → should pick settlement
    @Test
    void takeTurn_usesStrategy_preferringSettlement() {
        // road on edge 0 gives connectivity to nodes 0 and 1
        board.getEdges().get(0).setRoadOwner(p);

        p.getResourceHand().addResource(ResourceType.WOOD, 1);
        p.getResourceHand().addResource(ResourceType.BRICK, 1);
        p.getResourceHand().addResource(ResourceType.WHEAT, 1);
        p.getResourceHand().addResource(ResourceType.SHEEP, 1);

        BuildAction action = p.takeTurn(board);
        assertEquals(BuildType.SETTLEMENT, action.getType());
    }

    // custom strategy with only NonVpBuildRule → road (0.8) beats settlement (0.0)
    @Test
    void setStrategy_changesDecisionMaking() {
        p.setStrategy(new ImmediateValueDecisionStrategy(List.of(new NonVpBuildRule())));
        board.getEdges().get(0).setRoadOwner(p);

        p.getResourceHand().addResource(ResourceType.WOOD, 1);
        p.getResourceHand().addResource(ResourceType.BRICK, 1);
        p.getResourceHand().addResource(ResourceType.WHEAT, 1);
        p.getResourceHand().addResource(ResourceType.SHEEP, 1);

        BuildAction action = p.takeTurn(board);
        assertEquals(BuildType.ROAD, action.getType());
    }

    // settlement exists + city resources → should upgrade to city
    @Test
    void takeTurn_city_whenSettlementExists() {
        Node n = board.getNodes().get(0);
        n.setStructureOwner(p);
        n.setStructureType(StructureType.SETTLEMENT);

        p.getResourceHand().addResource(ResourceType.WHEAT, 2);
        p.getResourceHand().addResource(ResourceType.ORE, 3);

        BuildAction action = p.takeTurn(board);
        assertEquals(BuildType.CITY, action.getType());
    }

    // >7 cards should force the build-attempt branch
    @Test
    void takeTurn_withOver7Cards_stillBuilds() {
        Node n = board.getNodes().get(0);
        n.setStructureOwner(p);
        n.setStructureType(StructureType.SETTLEMENT);

        // 8 total cards, includes WOOD+BRICK for a road
        p.getResourceHand().addResource(ResourceType.WOOD, 2);
        p.getResourceHand().addResource(ResourceType.BRICK, 2);
        p.getResourceHand().addResource(ResourceType.WHEAT, 2);
        p.getResourceHand().addResource(ResourceType.SHEEP, 2);

        BuildAction action = p.takeTurn(board);
        assertNotEquals(BuildType.PASS, action.getType());
    }
}
