package CatanAssignment1;


public class ResourceHand {

    private ResourceMap resources = new ResourceMap();

    public int totalCards() {
        return resources.total();
    }

    public ResourceMap getResources() {
        return resources;
    }

    public boolean canAfford(ResourceMap cost) {
        return resources.contains(cost);
    }

    public void addResource(ResourceType type, int amount) {
        resources.add(type, amount);
    }

    public void deduct(ResourceMap cost) {
        resources.deduct(cost);
    }
}
