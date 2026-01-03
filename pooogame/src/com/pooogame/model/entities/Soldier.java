package com.pooogame.model.entities;

public class Soldier extends Unit {
    public Soldier(int ownerId) {
        // Name, HP, Atk, Def, Range, Cost, Symbol, Owner
        super("Soldier", 100, 15, 5, 1, 50, "S", ownerId);
    }
}
