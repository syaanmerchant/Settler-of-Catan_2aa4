package CatanAssignment1;

public class Edge {

    // UML: - id: Integer [1]
    private Integer id;

    // UML: - roadOwner: Player [0..1]
    private Player roadOwner;

    // UML association role: + board (1)
    private Board board;

    // UML association role: + endpoints (2)
    private Node[] endpoints = new Node[2];
}
