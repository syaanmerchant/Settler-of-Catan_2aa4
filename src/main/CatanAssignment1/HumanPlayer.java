package CatanAssignment1;

import java.util.Scanner;

/**
 * Human-controlled player that reads commands from an input stream and uses a
 * {@link CommandParser} to act each turn.
 */
public class HumanPlayer extends Player {

    private final CommandParser parser;
    private final Scanner scanner;

    public HumanPlayer(int id, CommandParser parser, Scanner scanner) {
        super(id);
        this.parser = parser;
        this.scanner = scanner;
    }

    /**
     * Assignment 2 human turn procedure:
     * - LIST can be used any time
     * - ROLL must happen once before any BUILD/GO
     * - BUILD commands may be issued after ROLL
     * - GO ends the turn (only after ROLL)
     */
    public void playTurn(int turnId, Board board, Simulator.HumanTurnContext ctx) {
        printHelp();
        boolean rolled = false;

        while (true) {
            System.out.print(turnId + " / P" + (getId() + 1)
                    + " enter command (Roll | List | Build ... | Go): ");

            if (!scanner.hasNextLine()) {
                System.out.println();
                return;
            }

            String line = scanner.nextLine();
            CommandParser.ParseResult result = parser.parse(line);

            if (!result.isValid()) {
                System.out.println("  Error: " + result.getErrorMessage());
                continue;
            }

            switch (result.getCommandType()) {
                case LIST:
                    printHand(rolled);
                    break;
                case ROLL:
                    if (rolled) {
                        System.out.println("  You already rolled this turn. Use Build ... or Go.");
                        break;
                    }
                    int roll = rollDice();
                    rolled = true;
                    ctx.applyRoll(this, roll);
                    System.out.println(turnId + " / P" + (getId() + 1) + ": ROLL " + roll);
                    break;
                case GO:
                    if (!rolled) {
                        System.out.println("  You must Roll before ending your turn with Go.");
                        break;
                    }
                    System.out.println(turnId + " / P" + (getId() + 1) + ": GO");
                    return;
                case BUILD_ROAD:
                case BUILD_SETTLEMENT:
                case BUILD_CITY:
                    if (!rolled) {
                        System.out.println("  You must Roll before building. Try: Roll");
                        break;
                    }
                    BuildAction action = toBuildAction(board, result);
                    if (action == null) {
                        break;
                    }
                    if (!board.validateBuild(this, action)) {
                        System.out.println("  That build is not allowed by the current game rules/state. Try again.");
                        break;
                    }
                    board.executeBuild(this, action);
                    System.out.println(turnId + " / P" + (getId() + 1) + ": " + action.describe());
                    break;
                default:
                    System.out.println("  Error: unsupported command type: " + result.getCommandType());
            }
        }
    }

    /**
     * Overloaded version that routes builds through CommandHistory
     * and supports Undo / Redo commands.
     */
    public void playTurn(int turnId, Board board, Simulator.HumanTurnContext ctx, CommandHistory history) {
        printHelp();
        boolean rolled = false;

        while (true) {
            System.out.print(turnId + " / P" + (getId() + 1)
                    + " enter command (Roll | List | Build ... | Undo | Redo | Go): ");

            if (!scanner.hasNextLine()) {
                System.out.println();
                return;
            }

            String line = scanner.nextLine();
            CommandParser.ParseResult result = parser.parse(line);

            if (!result.isValid()) {
                System.out.println("  Error: " + result.getErrorMessage());
                continue;
            }

            switch (result.getCommandType()) {
                case LIST:
                    printHand(rolled);
                    break;
                case ROLL:
                    if (rolled) {
                        System.out.println("  You already rolled this turn. Use Build ... or Go.");
                        break;
                    }
                    int roll = rollDice();
                    rolled = true;
                    ctx.applyRoll(this, roll);
                    System.out.println(turnId + " / P" + (getId() + 1) + ": ROLL " + roll);
                    break;
                case GO:
                    if (!rolled) {
                        System.out.println("  You must Roll before ending your turn with Go.");
                        break;
                    }
                    System.out.println(turnId + " / P" + (getId() + 1) + ": GO");
                    return;
                case BUILD_ROAD:
                case BUILD_SETTLEMENT:
                case BUILD_CITY:
                    if (!rolled) {
                        System.out.println("  You must Roll before building. Try: Roll");
                        break;
                    }
                    BuildAction action = toBuildAction(board, result);
                    if (action == null) {
                        break;
                    }
                    if (!board.validateBuild(this, action)) {
                        System.out.println("  That build is not allowed by the current game rules/state. Try again.");
                        break;
                    }
                    history.executeCommand(new BuildCommand(board, this, action));
                    System.out.println(turnId + " / P" + (getId() + 1) + ": " + action.describe());
                    break;
                case UNDO:
                    history.undo();
                    break;
                case REDO:
                    history.redo();
                    break;
                default:
                    System.out.println("  Error: unsupported command type: " + result.getCommandType());
            }
        }
    }

    /**
     * Step-forward gate: wait until the human enters GO.
     */
    public void waitForGoGate(int turnId) {
        while (true) {
            System.out.print(turnId + " / (step) type Go to proceed: ");
            if (!scanner.hasNextLine()) {
                System.out.println();
                return;
            }
            CommandParser.ParseResult r = parser.parse(scanner.nextLine());
            if (!r.isValid()) {
                System.out.println("  Error: " + r.getErrorMessage());
                continue;
            }
            if (r.getCommandType() == CommandParser.CommandType.GO) {
                return;
            }
            System.out.println("  Please type Go to proceed.");
        }
    }

    @Override
    public BuildAction takeTurn(Board board) {
        // Human turns are orchestrated by Simulator for A2 (ROLL/LIST/BUILD/GO).
        return new BuildAction(BuildType.PASS, null, null);
    }

    private void printHelp() {
        System.out.println();
        System.out.println("=== Human player controls ===");
        System.out.println("Examples of valid commands (case-insensitive):");
        System.out.println("  Roll                           -> roll dice and collect resources (or trigger robber on 7)");
        System.out.println("  List                           -> show your current hand");
        System.out.println("  Build road [11,12]             -> build a road between nodes 11 and 12");
        System.out.println("  Build settlement [5]           -> build a settlement at node 5");
        System.out.println("  Build city [7]                 -> upgrade your settlement at node 7 to a city");
        System.out.println("  Undo                           -> undo your last action");
        System.out.println("  Redo                           -> redo an undone action");
        System.out.println("  Go                             -> end your turn");
        System.out.println();
    }

    private void printHand(boolean rolled) {
        System.out.println("  Rolled this turn: " + rolled);
        System.out.println("  Hand: " + getResourceHand().getResources());
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

    private BuildAction toBuildAction(Board board, CommandParser.ParseResult result) {
        if (result.getCommandType() == CommandParser.CommandType.BUILD_ROAD) {
            int a = result.getNodeIdA();
            int b = result.getNodeIdB();
            Edge edge = findEdge(board, a, b);
            if (edge == null) {
                System.out.println("  Error: no edge found between nodes " + a + " and " + b + ".");
                return null;
            }
            return new BuildAction(BuildType.ROAD, null, edge);
        }

        if (result.getCommandType() == CommandParser.CommandType.BUILD_SETTLEMENT
                || result.getCommandType() == CommandParser.CommandType.BUILD_CITY) {
            int nodeId = result.getNodeId();
            if (nodeId < 0 || nodeId >= board.getNodes().size()) {
                System.out.println("  Error: node " + nodeId + " does not exist on this board.");
                return null;
            }
            Node node = board.getNodes().get(nodeId);
            BuildType type = result.getCommandType() == CommandParser.CommandType.BUILD_CITY ? BuildType.CITY
                    : BuildType.SETTLEMENT;
            return new BuildAction(type, node, null);
        }

        System.out.println("  Error: command is not a build command.");
        return null;
    }
}

