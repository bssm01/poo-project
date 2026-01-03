package com.pooogame.model.map;

import com.pooogame.model.entities.Building;
import com.pooogame.model.entities.Unit;

public class Tile {
    private int x, y;
    private TileType type;
    private Unit unit;
    private Building building;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.unit = null;
        this.building = null;
    }

    public boolean isOccupied() {
        return unit != null || building != null || !type.isAccessible();
    }

    // Getters and Setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
