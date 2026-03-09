package CatanAssignment1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses human text commands into structured build intentions.
 *
 * Syntax (case-insensitive, flexible whitespace):
 * - "road A-B"        -> build road between nodes A and B
 * - "settlement N"    -> build settlement at node N
 * - "city N"          -> upgrade settlement at node N to city
 * - "pass"            -> take no build action
 */
public class CommandParser {

    private static final Pattern ROAD_PATTERN =
            Pattern.compile("(?i)\\s*road\\s+(\\d+)\\s*-\\s*(\\d+)\\s*");
    private static final Pattern SETTLEMENT_PATTERN =
            Pattern.compile("(?i)\\s*settlement\\s+(\\d+)\\s*");
    private static final Pattern CITY_PATTERN =
            Pattern.compile("(?i)\\s*city\\s+(\\d+)\\s*");
    private static final Pattern PASS_PATTERN =
            Pattern.compile("(?i)\\s*pass\\s*");

    public static final class ParseResult {
        private final boolean valid;
        private final boolean pass;
        private final BuildType buildType;
        private final Integer nodeId;
        private final Integer nodeIdA;
        private final Integer nodeIdB;
        private final String errorMessage;

        private ParseResult(boolean valid,
                            boolean pass,
                            BuildType buildType,
                            Integer nodeId,
                            Integer nodeIdA,
                            Integer nodeIdB,
                            String errorMessage) {
            this.valid = valid;
            this.pass = pass;
            this.buildType = buildType;
            this.nodeId = nodeId;
            this.nodeIdA = nodeIdA;
            this.nodeIdB = nodeIdB;
            this.errorMessage = errorMessage;
        }

        public static ParseResult pass() {
            return new ParseResult(true, true, BuildType.PASS, null, null, null, null);
        }

        public static ParseResult road(int a, int b) {
            return new ParseResult(true, false, BuildType.ROAD, null, a, b, null);
        }

        public static ParseResult settlement(int nodeId) {
            return new ParseResult(true, false, BuildType.SETTLEMENT, nodeId, null, null, null);
        }

        public static ParseResult city(int nodeId) {
            return new ParseResult(true, false, BuildType.CITY, nodeId, null, null, null);
        }

        public static ParseResult error(String message) {
            return new ParseResult(false, false, null, null, null, null, message);
        }

        public boolean isValid() {
            return valid;
        }

        public boolean isPass() {
            return pass;
        }

        public BuildType getBuildType() {
            return buildType;
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
            return ParseResult.error("Empty command. Try 'road', 'settlement', 'city', or 'pass'.");
        }

        Matcher mPass = PASS_PATTERN.matcher(trimmed);
        if (mPass.matches()) {
            return ParseResult.pass();
        }

        Matcher mRoad = ROAD_PATTERN.matcher(trimmed);
        if (mRoad.matches()) {
            int a = Integer.parseInt(mRoad.group(1));
            int b = Integer.parseInt(mRoad.group(2));
            if (a == b) {
                return ParseResult.error("Road endpoints must be two different node ids.");
            }
            return ParseResult.road(a, b);
        }

        Matcher mSettlement = SETTLEMENT_PATTERN.matcher(trimmed);
        if (mSettlement.matches()) {
            int nodeId = Integer.parseInt(mSettlement.group(1));
            return ParseResult.settlement(nodeId);
        }

        Matcher mCity = CITY_PATTERN.matcher(trimmed);
        if (mCity.matches()) {
            int nodeId = Integer.parseInt(mCity.group(1));
            return ParseResult.city(nodeId);
        }

        return ParseResult.error(
                "Could not understand command. Examples: 'road 11-12', 'settlement 5', 'city 7', 'pass'.");
    }
}

