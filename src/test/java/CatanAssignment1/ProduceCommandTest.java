package CatanAssignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProduceCommandTest {

    private Board board;
    private Player p;
    private int roll;
    private ResourceType res;
    private Tile tile;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.setupMap();
        p = new MachinePlayer(0);

        // find a real tile so produce actually hands out resources
        tile = board.getTiles().stream()
                .filter(t -> t.getResourceType() != ResourceType.DESERT && t.getNumberToken() != 0)
                .findFirst()
                .orElseThrow();
        roll = tile.getNumberToken();
        res = tile.getResourceType();
        tile.getCornerNodes().get(0).setStructureOwner(p);
        tile.getCornerNodes().get(0).setStructureType(StructureType.SETTLEMENT);
    }

    // execute() should trigger produce and give the player at least 1 resource
    @Test
    void execute_increasesResources_forPlayerWithSettlement() {
        int before = p.getResourceHand().getResources().get(res);
        ProduceCommand cmd = new ProduceCommand(board, roll, List.of(p));
        cmd.execute();
        assertTrue(p.getResourceHand().getResources().get(res) > before);
    }

    // undo() should restore every player's hand to what it was before execute()
    @Test
    void undo_restoresAllPlayerHands() {
        // second player with a settlement on another corner of the same tile
        Player p2 = new MachinePlayer(1);
        tile.getCornerNodes().get(1).setStructureOwner(p2);
        tile.getCornerNodes().get(1).setStructureType(StructureType.SETTLEMENT);

        int before1 = p.getResourceHand().getResources().get(res);
        int before2 = p2.getResourceHand().getResources().get(res);

        ProduceCommand cmd = new ProduceCommand(board, roll, List.of(p, p2));
        cmd.execute();
        cmd.undo();

        assertEquals(before1, p.getResourceHand().getResources().get(res));
        assertEquals(before2, p2.getResourceHand().getResources().get(res));
    }

    // describe() should be non-null and include the roll number
    @Test
    void describe_returnsNonNull_containsRollNumber() {
        ProduceCommand cmd = new ProduceCommand(board, roll, List.of(p));
        String desc = cmd.describe();
        assertNotNull(desc);
        assertTrue(desc.contains(String.valueOf(roll)));
    }
}
