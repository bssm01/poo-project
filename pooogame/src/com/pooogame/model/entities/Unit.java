package com.pooogame.model.entities;

public abstract class Unit {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attack;
    protected int defense;
    protected int range;
    protected int cost;
    protected String symbol;
    protected int ownerId; // 0 for player, 1 for enemy

    public Unit(String name, int health, int attack, int defense, int range, int cost, String symbol, int ownerId) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.range = range;
        this.cost = cost;
        this.symbol = symbol;
        this.ownerId = ownerId;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0)
            this.health = 0;
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > maxHealth)
            this.health = maxHealth;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getRange() {
        return range;
    }

    public int getCost() {
        return cost;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
