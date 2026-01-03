package com.pooogame.model.entities;

public class Mine extends Building {
    public Mine(int ownerId) {
        super("Mine", 150, 2, "M", ownerId);
    }

    @Override
    public void performAction() {
        // Will be called each turn to add Stone/Gold
    }
}
