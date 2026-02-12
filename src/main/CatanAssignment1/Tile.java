package CatanAssignment1;


import java.util.ArrayList;
import java.util.List;

public class Tile {

    private int id;
    private ResourceType resourceType;
    private int numberToken; // 2-12, 0 for desert
    private List<Node> cornerNodes = new ArrayList<>();

    public Tile(int id, ResourceType resourceType, int numberToken) {
        this.id = id;
        this.resourceType = resourceType;
        this.numberToken = numberToken;
    }

    public int getId() {
        return id;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getNumberToken() {
        return numberToken;
    }

    public List<Node> getCornerNodes() {
        return cornerNodes;
    }

    public void addCornerNode(Node n) {
        if (n != null && !cornerNodes.contains(n)) {
            cornerNodes.add(n);
        }
    }
}
