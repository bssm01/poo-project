package com.pooogame.model.entities;

public class TrainingCamp extends Building {
    public TrainingCamp(int ownerId) {
        super("Training Camp", 200, 2, "T", ownerId);
    }

    @Override
    public void performAction() {
        // Allows recruiting
    }
}
