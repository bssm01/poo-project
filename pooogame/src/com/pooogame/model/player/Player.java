package com.pooogame.model.player;

import com.pooogame.model.ResourceType;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private int id;
    private Map<ResourceType, Integer> resources;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        this.resources = new HashMap<>();
        // Initial resources
        resources.put(ResourceType.GOLD, 100);
        resources.put(ResourceType.WOOD, 100);
        resources.put(ResourceType.STONE, 50);
        resources.put(ResourceType.FOOD, 50);
    }

    public void addResource(ResourceType type, int amount) {
        resources.put(type, resources.getOrDefault(type, 0) + amount);
    }

    public boolean hasResource(ResourceType type, int amount) {
        return resources.getOrDefault(type, 0) >= amount;
    }

    public void consumeResource(ResourceType type, int amount) {
        if (hasResource(type, amount)) {
            resources.put(type, resources.get(type) - amount);
        }
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
