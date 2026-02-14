package CatanAssignment1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {

	@Test
	void checkWinReturnsTrueWhenPlayerHas10VP() {
		Player p1 = new Player(0);
		Player p2 = new Player(1);

		p1.addVictoryPoints(10);  // make player win

		Simulator simulator = new Simulator(
			null,                     // board not needed for this test
			Arrays.asList(p1, p2),	
			10
		);

		assertTrue(simulator.checkWin());
	}

	@Test
	void checkWinReturnsFalseWhenNoPlayerHas10VP() {
		Player p1 = new Player(0);
		Player p2 = new Player(1);

		Simulator simulator = new Simulator(
                	null,
			Arrays.asList(p1, p2),
			10
		);
		
		assertFalse(simulator.checkWin());
	} 	

}	
