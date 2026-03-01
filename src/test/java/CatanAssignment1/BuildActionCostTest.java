package CatanAssignment1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BuildActionCostTest {

    // Locks down the road cost rule
    @Test
    void costFor_road_isWood1Brick1() {
        ResourceMap c = BuildAction.costFor(BuildType.ROAD);
        assertEquals(1, c.get(ResourceType.WOOD), "road cost should require 1 Wood");
        assertEquals(1, c.get(ResourceType.BRICK), "road cost should require 1 Brick");
    }

    // Locks down the settlement cost rule (one of each basic resource)
    @Test
    void costFor_settlement_isOneOfEachBasic() {
        ResourceMap c = BuildAction.costFor(BuildType.SETTLEMENT);
        assertEquals(1, c.get(ResourceType.WOOD), "settlement cost should require 1 Wood");
        assertEquals(1, c.get(ResourceType.BRICK), "settlement cost should require 1 Brick");
        assertEquals(1, c.get(ResourceType.WHEAT), "settlement cost should require 1 Wheat");
        assertEquals(1, c.get(ResourceType.SHEEP), "settlement cost should require 1 Sheep");
    }

    // Locks down the city upgrade cost rule (need 2 wheat and 3 ore).
    @Test
    void costFor_city_isWheat2Ore3() {
        ResourceMap c = BuildAction.costFor(BuildType.CITY);
        assertEquals(2, c.get(ResourceType.WHEAT), "city cost should require 2 Wheat");
        assertEquals(3, c.get(ResourceType.ORE), "city cost should require 3 Ore");
    }
}
