package CatanAssignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BoardProduceTest {

    private Board board;
    private Player p;

    @BeforeEach
    void setUp() {
        // Fresh board/player for each test
        board = new Board();
        board.setupMap();
        p = new MachinePlayer(0);
    }

    // Rolling a 7 produces nothing (robber logic handled elsewhere)
    @Test
    void produce_doesNothing_onSeven() {
        int before = p.getResourceHand().totalCards();
        board.produce(7);
        int after = p.getResourceHand().totalCards();
        assertEquals(before, after, "Rolling 7 should not produce resources");
    }

    // Settlement gains +1 resource from matching tile roll
    @Test
    void produce_givesOneResource_forSettlement_onMatchingTile() {
        // Find a non-desert tile and use its roll number
        Tile t = board.getTiles().stream()
                .filter(tile -> tile.getResourceType() != ResourceType.DESERT && tile.getNumberToken() != 0)
                .findFirst()
                .orElseThrow();

        int roll = t.getNumberToken();
        ResourceType res = t.getResourceType();

        // Put settlement for player on one corner of this tile
        Node corner = t.getCornerNodes().get(0);
        corner.setStructureOwner(p);
        corner.setStructureType(StructureType.SETTLEMENT);

        int before = p.getResourceHand().getResources().get(res);
        board.produce(roll);
        int after = p.getResourceHand().getResources().get(res);

        assertEquals(before + 1, after, "Settlement should gain 1 resource when tile number is rolled");
    }

    // City gains +2 resources from matching tile roll
    @Test
    void produce_givesTwoResources_forCity_onMatchingTile() {
        Tile t = board.getTiles().stream()
                .filter(tile -> tile.getResourceType() != ResourceType.DESERT && tile.getNumberToken() != 0)
                .findFirst()
                .orElseThrow();

        int roll = t.getNumberToken();
        ResourceType res = t.getResourceType();

        Node corner = t.getCornerNodes().get(0);
        corner.setStructureOwner(p);
        corner.setStructureType(StructureType.CITY);

        int before = p.getResourceHand().getResources().get(res);
        board.produce(roll);
        int after = p.getResourceHand().getResources().get(res);

        assertEquals(before + 2, after, "City should gain 2 resources when tile number is rolled");
    }

    // robber roll discards half from >7 hands and steals from adjacent player
    @Test
    void handleRobberRoll_discardsAndSteals() {
        Player roller = new MachinePlayer(0);
        Player victim = new MachinePlayer(1);

        // victim settlements on corners covering all 19 tiles
        int[] corners = {13, 15, 17, 19, 21, 22};
        for (int c : corners) {
            Node n = board.getNodes().get(c);
            n.setStructureOwner(victim);
            n.setStructureType(StructureType.SETTLEMENT);
        }
        victim.getResourceHand().addResource(ResourceType.WOOD, 5);

        // >7 cards triggers the discard-half path
        roller.getResourceHand().addResource(ResourceType.WHEAT, 8);

        List<Player> all = List.of(roller, victim);
        board.handleRobberRoll(roller, all, new Random(0));

        // roller: 8 wheat − 4 discarded + 1 stolen wood = 5
        assertEquals(5, roller.getResourceHand().totalCards());
        assertEquals(4, victim.getResourceHand().totalCards());
    }
}
