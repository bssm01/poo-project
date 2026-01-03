package com.pooogame.model.map;

public enum TileType {
    GRASS("."),
    WATER("~"),
    MOUNTAIN("^");

    private final String symbol;

    TileType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isAccessible() {
        return this != WATER;
    }
    
    public double getDefenseBonus() {
        if (this == MOUNTAIN) return 1.5;
        return 1.0;
    }
}
