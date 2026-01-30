package CatanAssignment1;

import java.util.ArrayList;
import java.util.List;

public class Board {

    // UML association role: + simulator (1)
    private Simulator simulator;

    // UML composition roles: + tiles (19), + nodes (54), + edges (*)
    private List<Tile> tiles = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    // UML: + setupMap()
    public void setupMap() {
        // Placeholder: create tiles/nodes/edges and wire associations.
    }

    // UML: + produce( in roll: Integer)
    public void produce(Integer roll) {
        // Placeholder: trigger tile production based on roll.
        // UML has Tile.produce() as well.
        if (roll == null) return;
        for (Tile t : tiles) {
            if (t != null) {
                t.produce();
            }
        }
    }

    // UML: + validateBuild( in p: Player, in action: BuildAction): Boolean
    public Boolean validateBuild(Player p, BuildAction action) {
        // Placeholder validation
        return (p != null && action != null);
    }

    // UML: + getIncidentEdges( in n: Node): Edge
    public Edge getIncidentEdges(Node n) {
        // UML return type is Edge (singular). Placeholder: return first found.
        if (n == null) return null;
        for (Edge e : edges) {
            if (e != null) return e;
        }
        return null;
    }

    // UML: + getNeighbourNodes( in n: Node): Node
    public Node getNeighbourNodes(Node n) {
        // UML return type is Node (singular). Placeholder: return first other node.
        if (n == null) return null;
        for (Node other : nodes) {
            if (other != null && other != n) return other;
        }
        return null;
    }
}
