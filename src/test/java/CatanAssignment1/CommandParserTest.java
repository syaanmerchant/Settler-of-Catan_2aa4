package CatanAssignment1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandParserTest {

    private final CommandParser parser = new CommandParser();

    @Test
    void roll_parsesIgnoringCaseAndWhitespace() {
        CommandParser.ParseResult r1 = parser.parse("Roll");
        assertTrue(r1.isValid());
        assertEquals(CommandParser.CommandType.ROLL, r1.getCommandType());
        assertNull(r1.getNodeId());
        assertNull(r1.getNodeIdA());
        assertNull(r1.getNodeIdB());

        CommandParser.ParseResult r2 = parser.parse("  rOlL  ");
        assertTrue(r2.isValid());
        assertEquals(CommandParser.CommandType.ROLL, r2.getCommandType());
    }

    @Test
    void go_and_list_parse() {
        CommandParser.ParseResult go = parser.parse("Go");
        assertTrue(go.isValid());
        assertEquals(CommandParser.CommandType.GO, go.getCommandType());

        CommandParser.ParseResult list = parser.parse("  LiSt ");
        assertTrue(list.isValid());
        assertEquals(CommandParser.CommandType.LIST, list.getCommandType());
    }

    @Test
    void buildSettlement_parsesBracketedNodeId() {
        CommandParser.ParseResult r = parser.parse("Build settlement [7]");
        assertTrue(r.isValid());
        assertEquals(CommandParser.CommandType.BUILD_SETTLEMENT, r.getCommandType());
        assertEquals(7, r.getNodeId());
        assertNull(r.getNodeIdA());
        assertNull(r.getNodeIdB());
    }

    @Test
    void buildCity_parsesBracketedNodeId() {
        CommandParser.ParseResult r = parser.parse("  build  city [  3 ] ");
        assertTrue(r.isValid());
        assertEquals(CommandParser.CommandType.BUILD_CITY, r.getCommandType());
        assertEquals(3, r.getNodeId());
        assertNull(r.getNodeIdA());
        assertNull(r.getNodeIdB());
    }

    @Test
    void buildRoad_parsesBracketedEndpoints() {
        CommandParser.ParseResult r1 = parser.parse("Build road [11,12]");
        assertTrue(r1.isValid());
        assertEquals(CommandParser.CommandType.BUILD_ROAD, r1.getCommandType());
        assertNull(r1.getNodeId());
        assertEquals(11, r1.getNodeIdA());
        assertEquals(12, r1.getNodeIdB());

        CommandParser.ParseResult r2 = parser.parse(" build ROAD [ 5 , 9 ] ");
        assertTrue(r2.isValid());
        assertEquals(CommandParser.CommandType.BUILD_ROAD, r2.getCommandType());
        assertNull(r2.getNodeId());
        assertEquals(5, r2.getNodeIdA());
        assertEquals(9, r2.getNodeIdB());
    }

    @Test
    void invalidCommand_returnsErrorResult() {
        CommandParser.ParseResult r = parser.parse("pass");
        assertFalse(r.isValid());
        assertNull(r.getCommandType());
        assertNotNull(r.getErrorMessage());
        assertTrue(r.getErrorMessage().toLowerCase().contains("could not understand"));
    }

    @Test
    void emptyOrWhitespaceInput_isRejectedWithHelpfulMessage() {
        CommandParser.ParseResult r1 = parser.parse("");
        assertFalse(r1.isValid());
        assertNull(r1.getCommandType());
        assertNotNull(r1.getErrorMessage());
        assertTrue(r1.getErrorMessage().toLowerCase().contains("empty command"));

        CommandParser.ParseResult r2 = parser.parse("   ");
        assertFalse(r2.isValid());
        assertNull(r2.getCommandType());
        assertNotNull(r2.getErrorMessage());
        assertTrue(r2.getErrorMessage().toLowerCase().contains("empty command"));
    }

    @Test
    void nullInput_isRejectedWithExplicitMessage() {
        CommandParser.ParseResult r = parser.parse(null);
        assertFalse(r.isValid());
        assertNull(r.getCommandType());
        assertNotNull(r.getErrorMessage());
        assertTrue(r.getErrorMessage().toLowerCase().contains("input was null"));
    }

    @Test
    void roadWithSameEndpoints_isRejected() {
        CommandParser.ParseResult r = parser.parse("Build road [5,5]");
        assertFalse(r.isValid());
        assertNull(r.getCommandType());
        assertNotNull(r.getErrorMessage());
    }

    @Test
    void buildRoad_withExtraTrailingTokens_isRejectedByRegex() {
        CommandParser.ParseResult r = parser.parse("Build road [1,2] extra");
        assertFalse(r.isValid());
        assertNull(r.getCommandType());
        assertNotNull(r.getErrorMessage());
    }
}

