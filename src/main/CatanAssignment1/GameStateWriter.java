package CatanAssignment1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Serializes the current board and player state into a JSON file compatible
 * with the Python visualizer's state.json format.
 */
public class GameStateWriter {

    private final Path path;

    public GameStateWriter(String path) {
        this.path = Path.of(path);
    }

    public void write(Board board, List<Player> players) {
        String json = buildJson(board, players);
        try {
            Files.writeString(path, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write game state JSON to " + path, e);
        }
    }

    private String buildJson(Board board, List<Player> players) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append(" \"roads\": [],\n");
        sb.append(" \"buildings\": []\n");
        sb.append("}\n");
        return sb.toString();
    }

    private String ownerName(Player p) {
        int id = p.getId();
        String[] palette = { "RED", "BLUE", "ORANGE", "WHITE" };
        if (id >= 0 && id < palette.length) {
            return palette[id];
        }
        return "P" + (id + 1);
    }
}
