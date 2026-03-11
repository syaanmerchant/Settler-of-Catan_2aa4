package CatanAssignment1;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    public void discardRandom(int count, Random rng) {
        if (count <= 0) {
            return;
        }
        Random random = (rng != null) ? rng : new Random();
        for (int i = 0; i < count; i++) {
            removeRandomCard(random);
        }
    }

    public Optional<ResourceType> removeRandomCard(Random rng) {
        Random random = (rng != null) ? rng : new Random();
        List<ResourceType> pool = new ArrayList<>();
        for (ResourceType rt : ResourceType.values()) {
            if (rt == ResourceType.DESERT) {
                continue;
            }
            int amt = resources.get(rt);
            for (int i = 0; i < amt; i++) {
                pool.add(rt);
            }
        }
        if (pool.isEmpty()) {
            return Optional.empty();
        }
        ResourceType chosen = pool.get(random.nextInt(pool.size()));
        resources.add(chosen, -1);
        return Optional.of(chosen);
    }
}
