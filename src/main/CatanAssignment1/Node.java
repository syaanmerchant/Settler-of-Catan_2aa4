package main.CatanAssignment1;


import java.util.ArrayList;
import java.util.List;

public class Node {

    private int id;
    private Player structureOwner;
    private StructureType structureType = StructureType.NONE;
    private List<Edge> incidentEdges = new ArrayList<>();
    private List<Node> neighbourNodes = new ArrayList<>();

    public Node(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Player getStructureOwner() {
        return structureOwner;
    }

    public StructureType getStructureType() {
        return structureType;
    }

    public void setStructureOwner(Player owner) {
        this.structureOwner = owner;
    }

    public void setStructureType(StructureType type) {
        this.structureType = type;
    }

    public List<Edge> getIncidentEdges() {
        return incidentEdges;
    }

    public List<Node> getNeighbourNodes() {
        return neighbourNodes;
    }

    public boolean isEmpty() {
        return structureType == StructureType.NONE || structureOwner == null;
    }
}
