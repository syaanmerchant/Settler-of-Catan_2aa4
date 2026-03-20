package CatanAssignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MachineDecisionStrategyTest {

    private ImmediateValueDecisionStrategy strategy;
    private MachinePlayer p;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.setupMap();
        p = new MachinePlayer(0);
        // default strategy matching what MachinePlayer uses internally
        strategy = new ImmediateValueDecisionStrategy(
                List.of(new VpEarningRule(), new NonVpBuildRule(), new LowHandAfterSpendRule()));
    }

    // SETTLEMENT scores 1.0, ROAD scores 0.8 → strategy should pick SETTLEMENT
    @Test
    void chooseAction_returnsSettlement_overRoad_whenBothAvailable() {
        Node n = board.getNodes().get(0);
        Edge e = board.getEdges().get(0);
        BuildAction chosen = strategy.chooseAction(p, board,
                List.of(new BuildAction(BuildType.ROAD, null, e),
                        new BuildAction(BuildType.SETTLEMENT, n, null)));
        assertEquals(BuildType.SETTLEMENT, chosen.getType());
    }

    // single candidate → strategy just returns it
    @Test
    void chooseAction_returnsRoad_whenOnlyRoadAffordable() {
        Edge e = board.getEdges().get(0);
        BuildAction chosen = strategy.chooseAction(p, board,
                List.of(new BuildAction(BuildType.ROAD, null, e)));
        assertEquals(BuildType.ROAD, chosen.getType());
    }

    // empty candidate list → falls back to PASS
    @Test
    void chooseAction_returnsPass_whenNoCandidates() {
        BuildAction chosen = strategy.chooseAction(p, board, List.of());
        assertEquals(BuildType.PASS, chosen.getType());
    }

    // with only LowHandAfterSpendRule: ROAD (8→6 cards, ≥5) scores 0, SETTLEMENT (8→4, <5) scores 0.5
    @Test
    void chooseAction_prefersLowHandRule_whenHandOver5() {
        ImmediateValueDecisionStrategy lowHand = new ImmediateValueDecisionStrategy(
                List.of(new LowHandAfterSpendRule()));
        p.getResourceHand().addResource(ResourceType.WOOD, 4);
        p.getResourceHand().addResource(ResourceType.BRICK, 4);

        Node n = board.getNodes().get(0);
        Edge e = board.getEdges().get(0);
        BuildAction chosen = lowHand.chooseAction(p, board,
                List.of(new BuildAction(BuildType.ROAD, null, e),
                        new BuildAction(BuildType.SETTLEMENT, n, null)));
        assertEquals(BuildType.SETTLEMENT, chosen.getType());
    }

    // null candidates list → should return PASS
    @Test
    void chooseAction_nullCandidates_returnsPass() {
        BuildAction chosen = strategy.chooseAction(p, board, null);
        assertEquals(BuildType.PASS, chosen.getType());
    }

    // two roads score equally under NonVpBuildRule → tie-break picks one
    @Test
    void chooseAction_tieBreaking_returnsSomeAction() {
        ImmediateValueDecisionStrategy roadOnly = new ImmediateValueDecisionStrategy(
                List.of(new NonVpBuildRule()));
        Edge e1 = board.getEdges().get(0);
        Edge e2 = board.getEdges().get(1);
        BuildAction chosen = roadOnly.chooseAction(p, board,
                List.of(new BuildAction(BuildType.ROAD, null, e1),
                        new BuildAction(BuildType.ROAD, null, e2)));
        assertEquals(BuildType.ROAD, chosen.getType());
    }
}
