package main.CatanAssignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Board manages the Catan map: tiles, nodes, edges.
 * Uses the Beginners map layout (fixed topology).
 */
public class Board {

    private List<Tile> tiles = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    private Map<Node, List<Edge>> nodeToEdges = new HashMap<>();
    private Map<Node, List<Node>> nodeToNeighbours = new HashMap<>();
    private Map<Tile, List<Node>> tileToNodes = new HashMap<>();

    /**
     * Sets up the Catan board using the Beginners map topology.
     * Layout: 3-4-5-4-3 hex rows. Tiles have fixed resource types and number
     * tokens.
     */
    public void setupMap() {
        // Build the hex grid topology. Catan uses pointy-top hexagons in odd-r offset.
        // Rows 0-4 have 3,4,5,4,3 tiles respectively.
        // We create nodes at corner positions. Each hex has 6 corners.
        // Corner (r,c) in a doubled coordinate system: r in [0..8], c in [0..10]
        // approx.

        // Create 54 nodes (indices 0-53)
        for (int i = 0; i < 54; i++) {
            nodes.add(new Node(i));
        }

        // Define edges as (nodeA_id, nodeB_id). 72 edges total.
        // This defines the Catan board graph - corners connected by edges.
        int[][] edgeDefs = getCatanEdgeDefinitions();

        for (int i = 0; i < edgeDefs.length; i++) {
            Node a = nodes.get(edgeDefs[i][0]);
            Node b = nodes.get(edgeDefs[i][1]);
            Edge e = new Edge(i, a, b);
            edges.add(e);
            a.getIncidentEdges().add(e);
            b.getIncidentEdges().add(e);
            a.getNeighbourNodes().add(b);
            b.getNeighbourNodes().add(a);
        }

        // Define tiles: each has 6 corner node indices. 19 tiles.
        int[][] tileCorners = getCatanTileCorners();

        // Beginners map: resource types and number tokens per tile (id 0-18)
        // Format: {resourceType ordinal, numberToken}. ResourceType: 0=WOOD, 1=BRICK,
        // 2=WHEAT, 3=SHEEP, 4=ORE, 5=DESERT
        int[][] tileData = {
                { 0, 9 }, // 0: Wood, 9
                { 3, 2 }, // 1: Sheep, 2
                { 1, 8 }, // 2: Brick, 8
                { 0, 10 }, // 3: Wood, 10
                { 2, 5 }, // 4: Wheat, 5
                { 4, 12 }, // 5: Ore, 12
                { 3, 5 }, // 6: Sheep, 5
                { 1, 4 }, // 7: Brick, 4
                { 2, 11 }, // 8: Wheat, 11
                { 0, 6 }, // 9: Wood, 6
                { 4, 10 }, // 10: Ore, 10
                { 3, 9 }, // 11: Sheep, 9
                { 5, 0 }, // 12: Desert (center), no token
                { 2, 11 }, // 13: Wheat, 11
                { 1, 3 }, // 14: Brick, 3
                { 4, 8 }, // 15: Ore, 8
                { 0, 4 }, // 16: Wood, 4
                { 3, 6 }, // 17: Sheep, 6
                { 2, 3 } // 18: Wheat, 3
        };

        for (int t = 0; t < 19; t++) {
            ResourceType rt = ResourceType.values()[tileData[t][0]];
            int numToken = tileData[t][1];
            Tile tile = new Tile(t, rt, numToken);
            for (int c = 0; c < 6; c++) {
                int nid = tileCorners[t][c];
                Node n = nodes.get(nid);
                tile.addCornerNode(n);
            }
            tiles.add(tile);
        }
    }

    /**
     * Returns the 72 edges as (nodeA, nodeB) index pairs for the standard Catan
     * board.
     */
    private int[][] getCatanEdgeDefinitions() {
        // Standard Catan hex grid - corners and edges.
        // Generated from the 3-4-5-4-3 layout. Each edge connects two adjacent corner
        // nodes.
        return new int[][] {
                { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 5, 6 }, { 6, 7 }, { 7, 8 }, { 8, 9 },
                { 9, 10 }, { 10, 11 }, { 0, 12 }, { 1, 13 }, { 2, 14 }, { 3, 15 }, { 4, 16 }, { 5, 17 }, { 6, 18 },
                { 7, 19 }, { 8, 20 }, { 9, 21 }, { 10, 22 }, { 11, 23 },
                { 12, 13 }, { 13, 14 }, { 14, 15 }, { 15, 16 }, { 16, 17 }, { 17, 18 }, { 18, 19 }, { 19, 20 },
                { 20, 21 }, { 21, 22 }, { 22, 23 },
                { 12, 24 }, { 13, 25 }, { 14, 26 }, { 15, 27 }, { 16, 28 }, { 17, 29 }, { 18, 30 }, { 19, 31 },
                { 20, 32 }, { 21, 33 }, { 22, 34 }, { 23, 35 },
                { 24, 25 }, { 25, 26 }, { 26, 27 }, { 27, 28 }, { 28, 29 }, { 29, 30 }, { 30, 31 }, { 31, 32 },
                { 32, 33 }, { 33, 34 }, { 34, 35 },
                { 24, 36 }, { 25, 37 }, { 26, 38 }, { 27, 39 }, { 28, 40 }, { 29, 41 }, { 30, 42 }, { 31, 43 },
                { 32, 44 }, { 33, 45 }, { 34, 46 }, { 35, 47 },
                { 36, 37 }, { 37, 38 }, { 38, 39 }, { 39, 40 }, { 40, 41 }, { 41, 42 }, { 42, 43 }, { 43, 44 },
                { 44, 45 }, { 45, 46 }, { 46, 47 },
                { 36, 48 }, { 37, 49 }, { 38, 50 }, { 39, 51 }, { 40, 52 }, { 41, 53 }, { 48, 49 }, { 49, 50 },
                { 50, 51 }, { 51, 52 }, { 52, 53 }
        };
    }

    /**
     * Returns for each tile (0-18) the 6 corner node indices in clockwise order.
     */
    private int[][] getCatanTileCorners() {
        // Each row of 6 values are the node IDs at the corners of that tile.
        // Matching the edge structure above.
        return new int[][] {
                { 0, 1, 13, 12, 24, 25 }, // 0
                { 1, 2, 14, 13, 25, 26 }, // 1
                { 2, 3, 15, 14, 26, 27 }, // 2
                { 3, 4, 16, 15, 27, 28 }, // 3
                { 4, 5, 17, 16, 28, 29 }, // 4
                { 5, 6, 18, 17, 29, 30 }, // 5
                { 6, 7, 19, 18, 30, 31 }, // 6
                { 7, 8, 20, 19, 31, 32 }, // 7
                { 8, 9, 21, 20, 32, 33 }, // 8
                { 9, 10, 22, 21, 33, 34 }, // 9
                { 10, 11, 23, 22, 34, 35 }, // 10
                { 12, 13, 25, 24, 36, 37 }, // 11
                { 13, 14, 26, 25, 37, 38 }, // 12
                { 14, 15, 27, 26, 38, 39 }, // 13
                { 15, 16, 28, 27, 39, 40 }, // 14
                { 16, 17, 29, 28, 40, 41 }, // 15
                { 17, 18, 30, 29, 41, 42 }, // 16
                { 18, 19, 31, 30, 42, 43 }, // 17
                { 19, 20, 32, 31, 43, 44 }, // 18
        };
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Resource production: when roll is rolled, tiles with that number produce.
     * Settlement = 1 resource, City = 2 resources. Skip desert and roll 7.
     */
    public void produce(int roll) {
        if (roll == 7)
            return;
        for (Tile tile : tiles) {
            if (tile.getResourceType() == ResourceType.DESERT || tile.getNumberToken() != roll)
                continue;
            ResourceType res = tile.getResourceType();
            for (Node n : tile.getCornerNodes()) {
                if (n.getStructureOwner() == null)
                    continue;
                Player owner = n.getStructureOwner();
                if (n.getStructureType() == StructureType.SETTLEMENT) {
                    owner.getResourceHand().addResource(res, 1);
                } else if (n.getStructureType() == StructureType.CITY) {
                    owner.getResourceHand().addResource(res, 2);
                }
            }
        }
    }

    /**
     * Validates a build action. Enforces invariants:
     * - Road: edge empty, connects to player's road/settlement/city
     * - Settlement: node empty, connected to player's road, distance rule (no
     * adjacent structures)
     * - City: node has player's settlement (upgrade)
     */
    public boolean validateBuild(Player p, BuildAction action) {
        if (p == null || action == null)
            return false;
        switch (action.getType()) {
            case PASS:
                return true;
            case ROAD:
                return validateRoadBuild(p, action.getEdge());
            case SETTLEMENT:
                return validateSettlementBuild(p, action.getNode());
            case CITY:
                return validateCityBuild(p, action.getNode());
            default:
                return false;
        }
    }

    private boolean validateRoadBuild(Player p, Edge e) {
        if (e == null || !e.isEmpty())
            return false;
        Node a = e.getNodeA(), b = e.getNodeB();
        // At least one endpoint must connect to p's road or settlement/city
        return playerConnectedToNode(p, a) || playerConnectedToNode(p, b);
    }

    private boolean playerConnectedToNode(Player p, Node n) {
        if (n == null)
            return false;
        if (n.getStructureOwner() == p)
            return true;
        for (Edge e : n.getIncidentEdges()) {
            if (e.getRoadOwner() == p)
                return true;
        }
        return false;
    }

    private boolean validateSettlementBuild(Player p, Node n) {
        if (n == null || !n.isEmpty())
            return false;
        if (!playerConnectedToNode(p, n))
            return false;
        // Distance rule: all 3 neighbour nodes must be empty
        for (Node neighbour : n.getNeighbourNodes()) {
            if (!neighbour.isEmpty())
                return false;
        }
        return true;
    }

    private boolean validateCityBuild(Player p, Node n) {
        if (n == null)
            return false;
        return n.getStructureOwner() == p && n.getStructureType() == StructureType.SETTLEMENT;
    }

    public List<Edge> getIncidentEdges(Node n) {
        return n != null ? n.getIncidentEdges() : new ArrayList<>();
    }

    public List<Node> getNeighbourNodes(Node n) {
        return n != null ? n.getNeighbourNodes() : new ArrayList<>();
    }

    /**
     * Executes a valid build: places road/settlement/city, deducts cost, updates
     * VPs.
     */
    public void executeBuild(Player p, BuildAction action) {
        if (!validateBuild(p, action))
            return;
        ResourceMap cost = action.cost();
        if (!p.getResourceHand().canAfford(cost))
            return;

        p.getResourceHand().deduct(cost);

        switch (action.getType()) {
            case ROAD:
                action.getEdge().setRoadOwner(p);
                break;
            case SETTLEMENT:
                action.getNode().setStructureOwner(p);
                action.getNode().setStructureType(StructureType.SETTLEMENT);
                p.addVictoryPoints(1);
                break;
            case CITY:
                action.getNode().setStructureType(StructureType.CITY);
                p.addVictoryPoints(1); // Settlement was 1 VP, City is 2, so +1
                break;
            case PASS:
                break;
        }
    }
}
