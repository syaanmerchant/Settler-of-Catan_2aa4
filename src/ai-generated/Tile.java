package CatanAssignment1;

import java.util.ArrayList;
import java.util.List;

public class Tile {

    // UML: - id: Integer [1]
    private Integer id;

    // UML: - resourceType: ResourceType [1]
    private ResourceType resourceType;

    // UML: - numberToken: Integer [1]
    private Integer numberToken;

    // UML association role: + board (1)
    private Board board;

    // UML association role-name shown: + node
    // (Multiplicity not shown; implemented as a list field with the UML role name.)
    private List<Node> node = new ArrayList<>();

    // UML: + produce()
    public void produce() {
        // Placeholder: production rules live at Board.produce(roll) typically.
    }
}
