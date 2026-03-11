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
    private final boolean strictExport;

    public GameStateWriter(String path) {
        this(path, false);
    }

    /**
     * @param strictExport when true, exports roads/buildings from the Java board model.
     *                     When false, exports an empty-but-valid schema that the
     *                     instructor visualizer can always load.
     */
    public GameStateWriter(String path, boolean strictExport) {
        this.path = Path.of(path);
        this.strictExport = strictExport;
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
        if (!strictExport) {
            sb.append(" \"roads\": [],\n");
            sb.append(" \"buildings\": []\n");
            sb.append("}\n");
            return sb.toString();
        }

        // Strict export mode: best-effort serialization of current state.
        sb.append(" \"roads\": [");
        boolean firstRoad = true;
        for (Edge e : board.getEdges()) {
            if (e.getRoadOwner() == null) {
                continue;
            }
            if (!firstRoad) {
                sb.append(",");
            }
            sb.append("\n  { \"a\": ").append(e.getNodeA().getId())
                    .append(", \"b\": ").append(e.getNodeB().getId())
                    .append(", \"owner\": \"").append(ownerName(e.getRoadOwner())).append("\" }");
            firstRoad = false;
        }
        if (!firstRoad) {
            sb.append("\n ");
        }
        sb.append("],\n");

        sb.append(" \"buildings\": [");
        boolean firstBuilding = true;
        for (Node n : board.getNodes()) {
            if (n.getStructureOwner() == null || n.getStructureType() == null || n.getStructureType() == StructureType.NONE) {
                continue;
            }
            if (!firstBuilding) {
                sb.append(",");
            }
            sb.append("\n  { \"node\": ").append(n.getId())
                    .append(", \"owner\": \"").append(ownerName(n.getStructureOwner())).append("\"")
                    .append(", \"type\": \"").append(n.getStructureType().name()).append("\" }");
            firstBuilding = false;
        }
        if (!firstBuilding) {
            sb.append("\n ");
        }
        sb.append("]\n");
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
