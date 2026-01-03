package com.pooogame.model.entities;

public class CommandCenter extends Building {
    public CommandCenter(int ownerId) {
        // Name, Cost, Time, Symbol, Owner
        super("Command Center", 500, 0, "C", ownerId);
    }

    @Override
    public void performAction() {
        // Generates small amount of resources or allows training
        // Logic will be handled in controller for now
    }
}
