package CatanAssignment1;


import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrator: main entry point for the Catan simulator.
 *
 * Invariants implemented:
 * - R1: Road connectivity: new roads must connect to existing roads or
 * settlements/cities of the same player
 * - R2: Distance rule: settlements/cities must be at least 2 intersections
 * apart (no adjacent structures)
 * - R3: City upgrade: cities replace existing settlements; the settlement piece
 * returns to supply
 * - R4: Roll 7: no resource production (robber ignored per spec)
 * - R5: Agents with >7 cards must try to build
 */
public class Demonstrator {

    public static void main(String[] args) {
        // 1. Parse config for max turns
        String configPath = "config/config.txt";
        if (args.length > 0) {
            configPath = args[0];
        }
        Config config = new Config(configPath);
        int maxRounds = config.getMaxTurns();

        // 2. Create Board and set up the hardcoded Beginners map (19 tiles, 54 nodes,
        // 72 edges)
        Board board = new Board();
        board.setupMap();

        // 3. Create 4 players and place initial settlements + roads
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new Player(i));
        }

        // Initial setup: 2 settlements + 2 roads per player (Beginners map positions)
        setupInitialPlacements(board, players);

        // 4. Give starting resources (from tiles adjacent to each player's second
        // settlement)
        grantStartingResources(board, players);

        // 5. Run simulation
        Simulator sim = new Simulator(board, players, maxRounds);
        sim.runSimulation();
    }

    /**
     * Places initial settlements and roads for all 4 players.
     * Uses fixed positions that satisfy the distance rule.
     */
    private static void setupInitialPlacements(Board board, List<Player> players) {
        // Chosen to satisfy the distance rule (no adjacent settlements), and (2) give each player access to a healthier
        // mix of resources, especially ensuring every player can produce WOOD and BRICK somewhere.

        // Player 0: settlements at nodes 3, 5; roads (3,2) and (5,6)
        placeInitial(board, players.get(0), new int[] { 3, 5 }, new int[][] { { 3, 2 }, { 5, 6 } });

        // Player 1: settlements at nodes 7, 9; roads (7,6) and (9,10)
        placeInitial(board, players.get(1), new int[] { 7, 9 }, new int[][] { { 7, 6 }, { 9, 10 } });

        // Player 2: settlements at nodes 1, 16; roads (1,0) and (16,28)
        placeInitial(board, players.get(2), new int[] { 1, 16 }, new int[][] { { 1, 0 }, { 16, 28 } });

        // Player 3: settlements at nodes 12, 31; roads (12,24) and (31,32)
        placeInitial(board, players.get(3), new int[] { 12, 31 }, new int[][] { { 12, 24 }, { 31, 32 } });
    }

    private static void placeInitial(Board board, Player p, int[] nodeIds, int[][] edgePairs) {
        for (int nid : nodeIds) {
            Node n = board.getNodes().get(nid);
            n.setStructureOwner(p);
            n.setStructureType(StructureType.SETTLEMENT);
            p.addVictoryPoints(1);
        }
        for (int[] pair : edgePairs) {
            Edge e = findEdge(board, pair[0], pair[1]);
            if (e != null)
                e.setRoadOwner(p);
        }
    }

    private static Edge findEdge(Board board, int nodeA, int nodeB) {
        for (Edge e : board.getEdges()) {
            int a = e.getNodeA().getId();
            int b = e.getNodeB().getId();
            if ((a == nodeA && b == nodeB) || (a == nodeB && b == nodeA))
                return e;
        }
        return null;
    }

    /**
     * Grants starting resources: 1 per resource type from tiles adjacent to each
     * player's second settlement.
     * Per Catan rules, players receive resources from the hexes around their second
     * (star) settlement.
     * Also grants 1 WOOD and 1 BRICK to each player so they can build roads
     * (ensures demo progression).
     */
    private static void grantStartingResources(Board board, List<Player> players) {
        // Second settlement node IDs (index 1) for each player
        int[] secondSettlementNodes = { 5, 9, 16, 31 };
        for (int i = 0; i < 4; i++) {
            Node n = board.getNodes().get(secondSettlementNodes[i]);
            for (Tile t : board.getTiles()) {
                if (t.getResourceType() == ResourceType.DESERT)
                    continue;
                if (t.getCornerNodes().contains(n)) {
                    players.get(i).getResourceHand().addResource(t.getResourceType(), 1);
                }
            }
            // Ensure each player can build at least one road (WOOD + BRICK)
            players.get(i).getResourceHand().addResource(ResourceType.WOOD, 1);
            players.get(i).getResourceHand().addResource(ResourceType.BRICK, 1);
        }
    }
}
