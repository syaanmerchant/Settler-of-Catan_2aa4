package CatanAssignment1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandParserTest {

    private final CommandParser parser = new CommandParser();

    @Test
    void roadCommand_parsesEndpoints_ignoringCaseAndWhitespace() {
        CommandParser.ParseResult r1 = parser.parse("road 11-12");
        assertTrue(r1.isValid());
        assertFalse(r1.isPass());
        assertEquals(BuildType.ROAD, r1.getBuildType());
        assertEquals(11, r1.getNodeIdA());
        assertEquals(12, r1.getNodeIdB());
        assertNull(r1.getNodeId());

        CommandParser.ParseResult r2 = parser.parse("  RoAd   5  -  9  ");
        assertTrue(r2.isValid());
        assertFalse(r2.isPass());
        assertEquals(BuildType.ROAD, r2.getBuildType());
        assertEquals(5, r2.getNodeIdA());
        assertEquals(9, r2.getNodeIdB());
        assertNull(r2.getNodeId());
    }

    @Test
    void settlementCommand_parsesSingleNodeId() {
        CommandParser.ParseResult r = parser.parse("settlement 7");
        assertTrue(r.isValid());
        assertFalse(r.isPass());
        assertEquals(BuildType.SETTLEMENT, r.getBuildType());
        assertEquals(7, r.getNodeId());
        assertNull(r.getNodeIdA());
        assertNull(r.getNodeIdB());
    }

    @Test
    void cityCommand_parsesSingleNodeId() {
        CommandParser.ParseResult r = parser.parse("  CiTy   3 ");
        assertTrue(r.isValid());
        assertFalse(r.isPass());
        assertEquals(BuildType.CITY, r.getBuildType());
        assertEquals(3, r.getNodeId());
        assertNull(r.getNodeIdA());
        assertNull(r.getNodeIdB());
    }

    @Test
    void passCommand_parsesAsPass_ignoringCaseAndWhitespace() {
        CommandParser.ParseResult r1 = parser.parse("pass");
        assertTrue(r1.isValid());
        assertTrue(r1.isPass());
        assertEquals(BuildType.PASS, r1.getBuildType());
        assertNull(r1.getNodeId());
        assertNull(r1.getNodeIdA());
        assertNull(r1.getNodeIdB());

        CommandParser.ParseResult r2 = parser.parse("   PaSs   ");
        assertTrue(r2.isValid());
        assertTrue(r2.isPass());
        assertEquals(BuildType.PASS, r2.getBuildType());
        assertNull(r2.getNodeId());
        assertNull(r2.getNodeIdA());
        assertNull(r2.getNodeIdB());
    }

    @Test
    void invalidCommand_returnsErrorResult() {
        CommandParser.ParseResult r = parser.parse("build 10");
        assertFalse(r.isValid());
        assertFalse(r.isPass());
        assertNull(r.getBuildType());
        assertNull(r.getNodeId());
        assertNull(r.getNodeIdA());
        assertNull(r.getNodeIdB());
        assertNotNull(r.getErrorMessage());
        assertTrue(r.getErrorMessage().toLowerCase().contains("could not understand"));
    }

    @Test
    void emptyOrWhitespaceInput_isRejectedWithHelpfulMessage() {
        CommandParser.ParseResult r1 = parser.parse("");
        assertFalse(r1.isValid());
        assertFalse(r1.isPass());
        assertNull(r1.getBuildType());
        assertNotNull(r1.getErrorMessage());
        assertTrue(r1.getErrorMessage().toLowerCase().contains("empty command"));

        CommandParser.ParseResult r2 = parser.parse("   ");
        assertFalse(r2.isValid());
        assertFalse(r2.isPass());
        assertNull(r2.getBuildType());
        assertNotNull(r2.getErrorMessage());
        assertTrue(r2.getErrorMessage().toLowerCase().contains("empty command"));
    }

    @Test
    void nullInput_isRejectedWithExplicitMessage() {
        CommandParser.ParseResult r = parser.parse(null);
        assertFalse(r.isValid());
        assertFalse(r.isPass());
        assertNull(r.getBuildType());
        assertNotNull(r.getErrorMessage());
        assertTrue(r.getErrorMessage().toLowerCase().contains("input was null"));
    }

    @Test
    void roadWithSameEndpoints_isRejected() {
        CommandParser.ParseResult r = parser.parse("road 5-5");
        assertFalse(r.isValid());
        assertFalse(r.isPass());
        assertNull(r.getBuildType());
        assertNotNull(r.getErrorMessage());
    }

    @Test
    void roadCommand_withExtraTrailingTokens_isRejectedByRegex() {
        CommandParser.ParseResult r = parser.parse("road 1-2 extra");
        assertFalse(r.isValid());
        assertFalse(r.isPass());
        assertNull(r.getBuildType());
        assertNotNull(r.getErrorMessage());
    }
}

