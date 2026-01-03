package com.pooogame.model.entities;

public class Farm extends Building {
    public Farm(int ownerId) {
        super("Farm", 100, 1, "F", ownerId);
    }

    @Override
    public void performAction() {
        // Will be called each turn to add Food
    }
}
