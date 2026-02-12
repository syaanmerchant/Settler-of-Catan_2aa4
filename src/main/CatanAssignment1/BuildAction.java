package CatanAssignment1;

public class BuildAction {

    private BuildType type;
    private Node node;
    private Edge edge;

    public BuildAction(BuildType type, Node node, Edge edge) {
        this.type = type;
        this.node = node;
        this.edge = edge;
    }

    public BuildType getType() {
        return type;
    }

    public Node getNode() {
        return node;
    }

    public Edge getEdge() {
        return edge;
    }

    public String describe() {
        if (type == BuildType.PASS)
            return "PASS";
        if (type == BuildType.ROAD && edge != null)
            return "ROAD E" + edge.getId();
        if (type == BuildType.SETTLEMENT && node != null)
            return "SETTLEMENT N" + node.getId();
        if (type == BuildType.CITY && node != null)
            return "CITY N" + node.getId();
        return "BuildAction{type=" + type + "}";
    }

    /** Returns the resource cost for a build type. */
    public static ResourceMap costFor(BuildType type) {
        ResourceMap c = new ResourceMap();
        if (type == null)
            return c;
        switch (type) {
            case ROAD:
                c.put(ResourceType.WOOD, 1);
                c.put(ResourceType.BRICK, 1);
                break;
            case SETTLEMENT:
                c.put(ResourceType.WOOD, 1);
                c.put(ResourceType.BRICK, 1);
                c.put(ResourceType.WHEAT, 1);
                c.put(ResourceType.SHEEP, 1);
                break;
            case CITY:
                c.put(ResourceType.WHEAT, 2);
                c.put(ResourceType.ORE, 3);
                break;
            case PASS:
            default:
                break;
        }
        return c;
    }

    public ResourceMap cost() {
        return costFor(type);
    }
}
