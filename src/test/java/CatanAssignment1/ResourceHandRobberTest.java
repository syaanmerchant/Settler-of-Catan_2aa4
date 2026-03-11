package CatanAssignment1;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceHandRobberTest {

    @Test
    void removeRandomCard_decreasesTotalWhenNonEmpty() {
        ResourceHand hand = new ResourceHand();
        hand.addResource(ResourceType.WOOD, 2);
        hand.addResource(ResourceType.BRICK, 1);

        int before = hand.totalCards();
        assertTrue(hand.removeRandomCard(new Random(1)).isPresent());
        int after = hand.totalCards();

        assertEquals(before - 1, after);
    }

    @Test
    void discardRandom_discardsHalfWhenMoreThanSeven() {
        ResourceHand hand = new ResourceHand();
        // 10 cards total
        hand.addResource(ResourceType.WOOD, 3);
        hand.addResource(ResourceType.BRICK, 3);
        hand.addResource(ResourceType.WHEAT, 2);
        hand.addResource(ResourceType.SHEEP, 2);

        assertEquals(10, hand.totalCards());
        hand.discardRandom(hand.totalCards() / 2, new Random(42));
        assertEquals(5, hand.totalCards());
    }
}

