package CatanAssignment1;

import java.util.EnumMap;
import java.util.Map;

public class ResourceMap {

    private final Map<ResourceType, Integer> map;

    public ResourceMap() {
        this.map = new EnumMap<>(ResourceType.class);
    }

    public ResourceMap(Map<ResourceType, Integer> initial) {
        this.map = new EnumMap<>(ResourceType.class);
        if (initial != null) {
            for (Map.Entry<ResourceType, Integer> e : initial.entrySet()) {
                if (e.getKey() != null && e.getValue() != null) {
                    this.map.put(e.getKey(), e.getValue());
                }
            }
        }
    }

    public Map<ResourceType, Integer> asMap() {
        return map;
    }

    public int get(ResourceType type) {
        Integer v = map.get(type);
        return (v == null) ? 0 : v;
    }

    public void put(ResourceType type, int amount) {
        map.put(type, Math.max(0, amount));
    }

    public void add(ResourceType type, int delta) {
        put(type, get(type) + delta);
    }

    public int total() {
        int sum = 0;
        for (Integer v : map.values()) {
            if (v != null) sum += v;
        }
        return sum;
    }
}
