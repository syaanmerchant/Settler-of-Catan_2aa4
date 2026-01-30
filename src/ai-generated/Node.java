package CatanAssignment1;

import java.util.ArrayList;
import java.util.List;

public class Node {

    // UML: - id: Integer [1]
    private Integer id;

    // UML: - structureOwner: Player [0..1]
    private Player structureOwner;

    // UML: - structureType: StructureType [1]
    private StructureType structureType;

    // UML association role: + board (1)
    private Board board;

    // UML association role-name shown: + tile
    // (Multiplicity not shown; implemented as a list field with the UML role name.)
    private List<Tile> tile = new ArrayList<>();

    // UML association role: + incidentEdges
    private List<Edge> incidentEdges = new ArrayList<>();
}
