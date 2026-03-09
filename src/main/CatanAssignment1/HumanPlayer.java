package CatanAssignment1;

import java.util.List;
import java.util.Scanner;

/**
 * Human-controlled player that reads commands from an input stream and uses a
 * {@link CommandParser} to decide its build action each turn.
 */
public class HumanPlayer extends Player {

    private final CommandParser parser;
    private final Scanner scanner;

    public HumanPlayer(int id, CommandParser parser, Scanner scanner) {
        super(id);
        this.parser = parser;
        this.scanner = scanner;
    }

    @Override
    public BuildAction takeTurn(Board board) {
        printHelp();

        while (true) {
            System.out.print("P" + (getId() + 1)
                    + " enter command (road A-B | settlement N | city N | pass): ");

            if (!scanner.hasNextLine()) {
                System.out.println();
                return new BuildAction(BuildType.PASS, null, null);
            }

            String line = scanner.nextLine();
            CommandParser.ParseResult result = parser.parse(line);

            if (!result.isValid()) {
                System.out.println("  Error: " + result.getErrorMessage());
                continue;
            }

            if (result.isPass()) {
                return new BuildAction(BuildType.PASS, null, null);
            }

            BuildType type = result.getBuildType();
            BuildAction action = null;

            if (type == BuildType.ROAD) {
                Integer aId = result.getNodeIdA();
                Integer bId = result.getNodeIdB();
                Edge edge = findEdge(board, aId, bId);
                if (edge == null) {
                    System.out.println("  Error: no edge found between nodes " + aId + " and " + bId + ".");
                    continue;
                }
                action = new BuildAction(BuildType.ROAD, null, edge);
            } else if (type == BuildType.SETTLEMENT || type == BuildType.CITY) {
                Integer nodeId = result.getNodeId();
                Node node = nodeById(board.getNodes(), nodeId);
                if (node == null) {
                    System.out
                            .println("  Error: node " + nodeId + " does not exist on this board.");
                    continue;
                }
                action = new BuildAction(type, node, null);
            } else {
                System.out.println("  Error: unsupported build type: " + type);
                continue;
            }

            if (!board.validateBuild(this, action)) {
                System.out.println("  That build is not allowed by the current game rules/state. Try again.");
                continue;
            }

            board.executeBuild(this, action);
            return action;
        }
    }

    private void printHelp() {
        System.out.println();
        System.out.println("=== Human player controls ===");
        System.out.println("Examples of valid commands (case-insensitive):");
        System.out.println("  road 11-12      -> build a road between nodes 11 and 12");
        System.out.println("  settlement 5    -> build a settlement at node 5");
        System.out.println("  city 7          -> upgrade your settlement at node 7 to a city");
        System.out.println("  pass            -> skip building this turn");
        System.out.println();
    }

    private static Node nodeById(List<Node> nodes, int id) {
        if (id < 0 || id >= nodes.size()) {
            return null;
        }
        return nodes.get(id);
    }

    private static Edge findEdge(Board board, int nodeA, int nodeB) {
        for (Edge e : board.getEdges()) {
            int a = e.getNodeA().getId();
            int b = e.getNodeB().getId();
            if ((a == nodeA && b == nodeB) || (a == nodeB && b == nodeA)) {
                return e;
            }
        }
        return null;
    }
}

