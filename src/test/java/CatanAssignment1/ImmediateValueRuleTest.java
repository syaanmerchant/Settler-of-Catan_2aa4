package CatanAssignment1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ImmediateValueRuleTest {

    // SETTLEMENT earns 1 VP → should return 1.0
    @Test
    void vpEarningRule_returns1_forSettlement() {
        BuildAction a = new BuildAction(BuildType.SETTLEMENT, null, null);
        assertEquals(1.0, new VpEarningRule().valueIfApplies(null, a));
    }

    // CITY also earns 1 VP → should return 1.0
    @Test
    void vpEarningRule_returns1_forCity() {
        BuildAction a = new BuildAction(BuildType.CITY, null, null);
        assertEquals(1.0, new VpEarningRule().valueIfApplies(null, a));
    }

    // ROAD is a non-VP build → should return 0.8
    @Test
    void nonVpBuildRule_returns0point8_forRoad() {
        BuildAction a = new BuildAction(BuildType.ROAD, null, null);
        assertEquals(0.8, new NonVpBuildRule().valueIfApplies(null, a));
    }

    // 6 cards, ROAD costs 2 → after = 4 < 5 → should return 0.5
    @Test
    void lowHandAfterSpendRule_returns0point5_whenSpendingBringsHandBelow5() {
        MachinePlayer p = new MachinePlayer(0);
        p.getResourceHand().addResource(ResourceType.WOOD, 3);
        p.getResourceHand().addResource(ResourceType.BRICK, 3);
        BuildAction a = new BuildAction(BuildType.ROAD, null, null);
        assertEquals(0.5, new LowHandAfterSpendRule().valueIfApplies(p, a));
    }

    // 10 cards, ROAD costs 2 → after = 8 ≥ 5 → should return 0.0
    @Test
    void lowHandAfterSpendRule_returnsZero_whenHandStaysAbove5() {
        MachinePlayer p = new MachinePlayer(0);
        p.getResourceHand().addResource(ResourceType.WOOD, 5);
        p.getResourceHand().addResource(ResourceType.BRICK, 5);
        BuildAction a = new BuildAction(BuildType.ROAD, null, null);
        assertEquals(0.0, new LowHandAfterSpendRule().valueIfApplies(p, a));
    }
}
