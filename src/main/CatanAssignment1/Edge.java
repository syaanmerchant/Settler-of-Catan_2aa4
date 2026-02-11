package main.CatanAssignment1;


public class Edge {

    private int id;
    private Player roadOwner;
    private Node nodeA;
    private Node nodeB;

    public Edge(int id, Node a, Node b) {
        this.id = id;
        this.nodeA = a;
        this.nodeB = b;
    }

    public int getId() {
        return id;
    }

    public Player getRoadOwner() {
        return roadOwner;
    }

    public Node getNodeA() {
        return nodeA;
    }

    public Node getNodeB() {
        return nodeB;
    }

    public void setRoadOwner(Player owner) {
        this.roadOwner = owner;
    }

    public boolean isEmpty() {
        return roadOwner == null;
    }

    /** Returns the other endpoint of this edge given one endpoint. */
    public Node getOther(Node n) {
        if (n == nodeA)
            return nodeB;
        if (n == nodeB)
            return nodeA;
        return null;
    }
}
