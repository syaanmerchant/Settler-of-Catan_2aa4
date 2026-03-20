package CatanAssignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BuildCommandTest {

    private Board board;
    private Player p;
    private Node node;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.setupMap();
        p = new MachinePlayer(0);

        // put a road on edge 0 so settlement validation passes for node 0
        board.getEdges().get(0).setRoadOwner(p);
        node = board.getNodes().get(0);

        // give player enough to afford a settlement
        p.getResourceHand().addResource(ResourceType.WOOD, 1);
        p.getResourceHand().addResource(ResourceType.BRICK, 1);
        p.getResourceHand().addResource(ResourceType.WHEAT, 1);
        p.getResourceHand().addResource(ResourceType.SHEEP, 1);
    }

    // execute() should place the settlement and award 1 VP
    @Test
    void execute_placesSettlement_andIncreasesVP() {
        BuildAction action = new BuildAction(BuildType.SETTLEMENT, node, null);
        BuildCommand cmd = new BuildCommand(board, p, action);

        int vpBefore = p.getVictoryPoints();
        cmd.execute();

        assertEquals(vpBefore + 1, p.getVictoryPoints());
        assertEquals(p, node.getStructureOwner());
        assertEquals(StructureType.SETTLEMENT, node.getStructureType());
    }

    // undo() should restore resources, VP, and clear the node back to empty
    @Test
    void undo_afterExecute_restoresResourcesVpAndNode() {
        BuildAction action = new BuildAction(BuildType.SETTLEMENT, node, null);
        BuildCommand cmd = new BuildCommand(board, p, action);

        int vpBefore = p.getVictoryPoints();
        int woodBefore = p.getResourceHand().getResources().get(ResourceType.WOOD);

        cmd.execute();
        cmd.undo();

        assertEquals(vpBefore, p.getVictoryPoints());
        assertEquals(woodBefore, p.getResourceHand().getResources().get(ResourceType.WOOD));
        assertNull(node.getStructureOwner());
        assertEquals(StructureType.NONE, node.getStructureType());
    }

    // describe() should be non-null and include the build type
    @Test
    void describe_returnsNonNull_containsBuildType() {
        BuildAction action = new BuildAction(BuildType.SETTLEMENT, node, null);
        BuildCommand cmd = new BuildCommand(board, p, action);

        String desc = cmd.describe();
        assertNotNull(desc);
        assertTrue(desc.contains("SETTLEMENT"));
    }

    // road build+undo should restore edge owner and resources
    @Test
    void execute_and_undo_road_restoresEdge() {
        // node 0 already has road on edge 0 via setUp → connectivity exists
        node.setStructureOwner(p);
        node.setStructureType(StructureType.SETTLEMENT);

        Edge e = board.getEdges().get(1);
        int woodBefore = p.getResourceHand().getResources().get(ResourceType.WOOD);

        BuildAction action = new BuildAction(BuildType.ROAD, null, e);
        BuildCommand cmd = new BuildCommand(board, p, action);
        cmd.execute();
        cmd.undo();

        assertNull(e.getRoadOwner());
        assertEquals(woodBefore, p.getResourceHand().getResources().get(ResourceType.WOOD));
    }

    // city build+undo should revert back to settlement and restore VP
    @Test
    void execute_city_and_undo_restoresSettlement() {
        node.setStructureOwner(p);
        node.setStructureType(StructureType.SETTLEMENT);
        p.addVictoryPoints(1); // the settlement's VP

        // give city cost (2 wheat + 3 ore)
        p.getResourceHand().addResource(ResourceType.WHEAT, 2);
        p.getResourceHand().addResource(ResourceType.ORE, 3);

        int vpBefore = p.getVictoryPoints();
        BuildAction action = new BuildAction(BuildType.CITY, node, null);
        BuildCommand cmd = new BuildCommand(board, p, action);

        cmd.execute();
        assertEquals(StructureType.CITY, node.getStructureType());

        cmd.undo();
        assertEquals(vpBefore, p.getVictoryPoints());
        assertEquals(StructureType.SETTLEMENT, node.getStructureType());
        assertEquals(p, node.getStructureOwner());
    }
}
