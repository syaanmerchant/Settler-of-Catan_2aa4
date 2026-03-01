package CatanAssignment1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceMapTest {

    // Having to pay nothing should always be affordable
    @Test
    void contains_returnsTrue_whenOtherIsNull() {
        ResourceMap hand = new ResourceMap();
        assertTrue(hand.contains(null), "contains(null) should be true by definition");
    }

    // Partition test: insufficient resources case should be rejected
    @Test
    void contains_returnsFalse_whenMissingRequiredResource() {
        ResourceMap hand = new ResourceMap();
        hand.put(ResourceType.WOOD, 0);

        ResourceMap cost = new ResourceMap();
        cost.put(ResourceType.WOOD, 1);

        assertFalse(hand.contains(cost), "Hand with 0 Wood should not contain cost needing 1 Wood");
    }

    // Adding resources should update per-type counts and total sum
    @Test
    void add_increasesAmount_andTotalReflectsSum() {
        ResourceMap m = new ResourceMap();
        m.add(ResourceType.WOOD, 2);
        m.add(ResourceType.BRICK, 1);

        assertEquals(3, m.total(), "Total should reflect sum of all resource counts");
        assertEquals(2, m.get(ResourceType.WOOD), "Wood count should be updated");
        assertEquals(1, m.get(ResourceType.BRICK), "Brick count should be updated");
    }

    // Payment behavior: deduct should reduce each required resource correctly
    @Test
    void deduct_reducesResources_whenContainsIsTrue() {
        ResourceMap hand = new ResourceMap();
        hand.put(ResourceType.WOOD, 2);
        hand.put(ResourceType.BRICK, 1);

        ResourceMap cost = new ResourceMap();
        cost.put(ResourceType.WOOD, 1);
        cost.put(ResourceType.BRICK, 1);

        assertTrue(hand.contains(cost), "Precondition: hand should contain the cost");
        hand.deduct(cost);

        assertEquals(1, hand.get(ResourceType.WOOD), "Wood should decrease by 1");
        assertEquals(0, hand.get(ResourceType.BRICK), "Brick should decrease by 1");
    }

    // Boundary: negative values should never be stored (should go to 0).
    @Test
    void put_neverStoresNegativeValues() {
        ResourceMap m = new ResourceMap();
        m.put(ResourceType.SHEEP, -10);
        assertEquals(0, m.get(ResourceType.SHEEP), "Negative values should clamp to 0");
    }
}
