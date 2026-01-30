package CatanAssignment1;

public class ResourceHand {

    // UML: - resources: ResourceMap [1]
    private ResourceMap resources = new ResourceMap();

    // UML association role: + player (1)
    private Player player;

    // UML: + totalCards(): Integer
    public Integer totalCards() {
        return resources.total();
    }
}
