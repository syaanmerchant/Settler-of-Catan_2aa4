package CatanAssignment1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses human text commands into structured intentions for Assignment 2.
 *
 * Supported commands (case-insensitive, flexible whitespace):
 * - Roll
 * - Go
 * - List
 * - Build settlement [nodeId]
 * - Build city [nodeId]
 * - Build road [fromNodeId,toNodeId]
 */
public class CommandParser {

    private static final Pattern ROLL_PATTERN = Pattern.compile("(?i)^\\s*roll\\s*$");
    private static final Pattern GO_PATTERN = Pattern.compile("(?i)^\\s*go\\s*$");
    private static final Pattern LIST_PATTERN = Pattern.compile("(?i)^\\s*list\\s*$");
    private static final Pattern UNDO_PATTERN = Pattern.compile("(?i)^\\s*undo\\s*$");
    private static final Pattern REDO_PATTERN = Pattern.compile("(?i)^\\s*redo\\s*$");

    private static final Pattern BUILD_SETTLEMENT_PATTERN =
            Pattern.compile("(?i)^\\s*build\\s+settlement\\s*\\[\\s*(\\d+)\\s*\\]\\s*$");
    private static final Pattern BUILD_CITY_PATTERN =
            Pattern.compile("(?i)^\\s*build\\s+city\\s*\\[\\s*(\\d+)\\s*\\]\\s*$");
    private static final Pattern BUILD_ROAD_PATTERN =
            Pattern.compile("(?i)^\\s*build\\s+road\\s*\\[\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\]\\s*$");

    public enum CommandType {
        ROLL,
        GO,
        LIST,
        BUILD_ROAD,
        BUILD_SETTLEMENT,
        BUILD_CITY,
        UNDO,
        REDO
    }

    public static final class ParseResult {
        private final boolean valid;
        private final CommandType commandType;
        private final Integer nodeId;
        private final Integer nodeIdA;
        private final Integer nodeIdB;
        private final String errorMessage;

        private ParseResult(boolean valid,
                            CommandType commandType,
                            Integer nodeId,
                            Integer nodeIdA,
                            Integer nodeIdB,
                            String errorMessage) {
            this.valid = valid;
            this.commandType = commandType;
            this.nodeId = nodeId;
            this.nodeIdA = nodeIdA;
            this.nodeIdB = nodeIdB;
            this.errorMessage = errorMessage;
        }

        public static ParseResult roll() {
            return new ParseResult(true, CommandType.ROLL, null, null, null, null);
        }

        public static ParseResult go() {
            return new ParseResult(true, CommandType.GO, null, null, null, null);
        }

        public static ParseResult list() {
            return new ParseResult(true, CommandType.LIST, null, null, null, null);
        }

        public static ParseResult buildRoad(int a, int b) {
            return new ParseResult(true, CommandType.BUILD_ROAD, null, a, b, null);
        }

        public static ParseResult buildSettlement(int nodeId) {
            return new ParseResult(true, CommandType.BUILD_SETTLEMENT, nodeId, null, null, null);
        }

        public static ParseResult buildCity(int nodeId) {
            return new ParseResult(true, CommandType.BUILD_CITY, nodeId, null, null, null);
        }

        public static ParseResult undo() {
            return new ParseResult(true, CommandType.UNDO, null, null, null, null);
        }

        public static ParseResult redo() {
            return new ParseResult(true, CommandType.REDO, null, null, null, null);
        }

        public static ParseResult error(String message) {
            return new ParseResult(false, null, null, null, null, message);
        }

        public boolean isValid() {
            return valid;
        }

        public CommandType getCommandType() {
            return commandType;
        }

        public Integer getNodeId() {
            return nodeId;
        }

        public Integer getNodeIdA() {
            return nodeIdA;
        }

        public Integer getNodeIdB() {
            return nodeIdB;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * Parses a single-line human command into a {@link ParseResult}.
     */
    public ParseResult parse(String input) {
        if (input == null) {
            return ParseResult.error("Input was null.");
        }

        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return ParseResult.error("Empty command. Try 'Roll', 'List', 'Build ...', or 'Go'.");
        }

        if (ROLL_PATTERN.matcher(trimmed).matches()) {
            return ParseResult.roll();
        }

        if (LIST_PATTERN.matcher(trimmed).matches()) {
            return ParseResult.list();
        }

        if (GO_PATTERN.matcher(trimmed).matches()) {
            return ParseResult.go();
        }

        if (UNDO_PATTERN.matcher(trimmed).matches()) {
            return ParseResult.undo();
        }

        if (REDO_PATTERN.matcher(trimmed).matches()) {
            return ParseResult.redo();
        }

        Matcher mBuildRoad = BUILD_ROAD_PATTERN.matcher(trimmed);
        if (mBuildRoad.matches()) {
            int a = Integer.parseInt(mBuildRoad.group(1));
            int b = Integer.parseInt(mBuildRoad.group(2));
            if (a == b) {
                return ParseResult.error("Road endpoints must be two different node ids.");
            }
            return ParseResult.buildRoad(a, b);
        }

        Matcher mBuildSettlement = BUILD_SETTLEMENT_PATTERN.matcher(trimmed);
        if (mBuildSettlement.matches()) {
            int nodeId = Integer.parseInt(mBuildSettlement.group(1));
            return ParseResult.buildSettlement(nodeId);
        }

        Matcher mBuildCity = BUILD_CITY_PATTERN.matcher(trimmed);
        if (mBuildCity.matches()) {
            int nodeId = Integer.parseInt(mBuildCity.group(1));
            return ParseResult.buildCity(nodeId);
        }

        return ParseResult.error("Could not understand command. Examples: 'Roll', 'List', 'Build road [11,12]', "
                + "'Build settlement [5]', 'Build city [7]', 'Undo', 'Redo', 'Go'.");
    }
}

