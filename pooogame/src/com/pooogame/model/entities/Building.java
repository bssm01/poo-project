package com.pooogame.model.entities;

public abstract class Building {
    protected String name;
    protected int constructionTime; // In turns
    protected int cost;
    protected String symbol;
    protected int ownerId;

    public Building(String name, int cost, int constructionTime, String symbol, int ownerId) {
        this.name = name;
        this.cost = cost;
        this.constructionTime = constructionTime;
        this.symbol = symbol;
        this.ownerId = ownerId;
    }

    public abstract void performAction(); // Produce resource or unit

    // Getters
    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getConstructionTime() {
        return constructionTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
