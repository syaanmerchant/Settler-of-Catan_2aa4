package CatanAssignment1;

public class BuildAction {

    // UML: - type: BuildType [1]
    private BuildType type;

    // UML: - node: Node [0..1]
    private Node node;

    // UML: - edge: Edge [0..1]
    private Edge edge;

    // UML: + describe(): String
    public String describe() {
        return "BuildAction{type=" + type + "}";
    }

    // UML: + cost(): ResourceMap
    public ResourceMap cost() {
        // Placeholder costs (common Catan costs)
        ResourceMap c = new ResourceMap();
        if (type == null) return c;

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
}
