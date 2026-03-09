package CatanAssignment1;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStateWriterTest {

    @Test
    void write_producesJsonWithRoadsAndBuildings() throws Exception {
        Board board = new Board();
        board.setupMap();

        Player p0 = new MachinePlayer(0);
        List<Player> players = new ArrayList<>();
        players.add(p0);

        // Give the player one road and one settlement so they appear in JSON.
        Edge edge = board.getEdges().get(0);
        edge.setRoadOwner(p0);

        Node node = board.getNodes().get(0);
        node.setStructureOwner(p0);
        node.setStructureType(StructureType.SETTLEMENT);

        Path temp = Files.createTempFile("catan-state", ".json");

        GameStateWriter writer = new GameStateWriter(temp.toString());
        writer.write(board, players);

        String json = Files.readString(temp, StandardCharsets.UTF_8);

        assertTrue(json.contains("\"roads\""));
        assertTrue(json.contains("\"buildings\""));
        assertTrue(json.contains("\"a\": " + edge.getNodeA().getId()));
        assertTrue(json.contains("\"b\": " + edge.getNodeB().getId()));
        assertTrue(json.contains("\"node\": " + node.getId()));
        assertTrue(json.contains("\"type\": \"SETTLEMENT\""));
        assertTrue(json.contains("\"owner\": \"RED\""));
    }
}

