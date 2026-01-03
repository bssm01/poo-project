package com.pooogame.controller;

import com.pooogame.model.entities.*;
import com.pooogame.model.map.*;
import com.pooogame.model.player.Player;
import com.pooogame.model.ResourceType;
import com.pooogame.view.ConsoleView;

import java.util.Random;

public class GameController {
    private GameMap map;
    private Player player;
    private Player enemy;
    private ConsoleView view;
    private boolean running;
    private int turnCount;

    // Selection state
    private Tile selectedTile;

    public GameController() {
        this.map = new GameMap(10, 10); // Standard size
        this.player = new Player("Human", 0);
        this.enemy = new Player("Machine", 1);
        this.view = new ConsoleView();
        this.running = true;
        this.turnCount = 1;

        initializeGame();
    }

    private void initializeGame() {
        // Clear start areas
        forceGrass(1, 1);
        forceGrass(1, 2);
        forceGrass(8, 8);
        forceGrass(8, 7);

        // Place initial Command Centers
        Tile p1Start = map.getTile(1, 1);
        p1Start.setBuilding(new CommandCenter(0)); // Player 0

        Tile p2Start = map.getTile(8, 8); // Far corner
        p2Start.setBuilding(new CommandCenter(1)); // Player 1 (Enemy)

        // Give initial units
        spawnUnit(new Soldier(0), 1, 2);
        spawnUnit(new Soldier(1), 8, 7);
    }

    private void forceGrass(int x, int y) {
        Tile t = map.getTile(x, y);
        if (t != null) {
            t.setType(TileType.GRASS);
        }
    }

    private void spawnUnit(Unit unit, int x, int y) {
        Tile t = map.getTile(x, y);
        if (t != null && !t.isOccupied()) {
            t.setUnit(unit);
        }
    }

    public void startGame() {
        view.displayMessage("Game Started!");
        while (running) {
            view.displayMap(map);
            view.displayPlayerInfo(player);
            view.displayMessage("Turn " + turnCount + " | Selected: "
                    + (selectedTile != null ? selectedTile.getX() + "," + selectedTile.getY() : "None"));

            String input = view.prompt(
                    "Command (select x y, move x y, attack x y, build [center|camp|farm|mine], recruit [soldier|archer|cavalier], next, exit):");
            processCommand(input);
        }
    }

    private void processCommand(String input) {
        String[] parts = input.trim().split(" ");
        String command = parts[0].toLowerCase();

        try {
            switch (command) {
                case "w":
                case "a":
                case "s":
                case "d":
                    handleWasdMovement(command);
                    break;
                case "f":
                    handleAttackKey();
                    break;
                case "select":
                    if (parts.length < 3)
                        throw new IllegalArgumentException("Usage: select x y");
                    int sx = Integer.parseInt(parts[1]);
                    int sy = Integer.parseInt(parts[2]);
                    selectTile(sx, sy);
                    break;
                case "move":
                    if (selectedTile == null || selectedTile.getUnit() == null) {
                        view.displayMessage("No unit selected!");
                        return;
                    }
                    if (parts.length < 3)
                        throw new IllegalArgumentException("Usage: move x y");
                    int mx = Integer.parseInt(parts[1]);
                    int my = Integer.parseInt(parts[2]);
                    moveUnit(selectedTile, mx, my);
                    break;
                case "attack":
                    if (selectedTile == null || selectedTile.getUnit() == null) {
                        view.displayMessage("No unit selected!");
                        return;
                    }
                    if (parts.length < 3)
                        throw new IllegalArgumentException("Usage: attack x y");
                    int ax = Integer.parseInt(parts[1]);
                    int ay = Integer.parseInt(parts[2]);
                    attackUnit(selectedTile, ax, ay);
                    break;
                case "build":
                    if (selectedTile == null) {
                        view.displayMessage("Select a tile first!");
                        return;
                    }
                    if (parts.length < 2)
                        throw new IllegalArgumentException("Usage: build [center|camp|farm|mine]");
                    buildStructure(selectedTile, parts[1]);
                    break;
                case "recruit":
                    if (selectedTile == null || selectedTile.getBuilding() == null) {
                        view.displayMessage("Select a building first!");
                        return;
                    }
                    if (parts.length < 2)
                        throw new IllegalArgumentException("Usage: recruit [soldier|archer|cavalier]");
                    recruitUnit(selectedTile, parts[1]);
                    break;
                case "next":
                    endTurn();
                    break;
                case "exit":
                case "quit":
                    running = false;
                    view.displayMessage("Game Over. Thanks for playing!");
                    break;
                case "help":
                    view.displayMessage(
                            "Commands: w/a/s/d (move), f (attack), select x y, move x y, attack x y, build [center|camp|farm|mine], recruit [soldier|archer|cavalier], next, exit");
                    break;
                default:
                    view.displayMessage("Unknown command. Type 'help'.");
            }
        } catch (NumberFormatException e) {
            view.displayMessage("Invalid numbers provided.");
        } catch (IllegalArgumentException e) {
            view.displayMessage(e.getMessage());
        } catch (Exception e) {
            view.displayMessage("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleAttackKey() {
        if (selectedTile == null || selectedTile.getUnit() == null) {
            view.displayMessage("No unit selected!");
            return;
        }

        Unit attacker = selectedTile.getUnit();
        int range = attacker.getRange();
        int sx = selectedTile.getX();
        int sy = selectedTile.getY();

        // Scan for enemies in range
        for (int y = sy - range; y <= sy + range; y++) {
            for (int x = sx - range; x <= sx + range; x++) {
                if (x == sx && y == sy)
                    continue; // Skip self

                int dist = Math.abs(sx - x) + Math.abs(sy - y);
                if (dist > range)
                    continue;

                Tile target = map.getTile(x, y);
                if (target != null && target.getUnit() != null) {
                    if (target.getUnit().getOwnerId() != player.getId()) {
                        // Found an enemy! Attack it.
                        attackUnit(selectedTile, x, y);
                        return; // Attack only one target
                    }
                }
            }
        }
        view.displayMessage("No enemy in range!");
    }

    private void handleWasdMovement(String key) {
        if (selectedTile == null || selectedTile.getUnit() == null) {
            view.displayMessage("No unit selected!");
            return;
        }

        int x = selectedTile.getX();
        int y = selectedTile.getY();

        switch (key) {
            case "w":
                y--;
                break;
            case "a":
                x--;
                break;
            case "s":
                y++;
                break;
            case "d":
                x++;
                break;
        }

        moveUnit(selectedTile, x, y);
    }

    private void selectTile(int x, int y) {
        Tile t = map.getTile(x, y);
        if (t == null) {
            view.displayMessage("Invalid coordinates.");
        } else {
            selectedTile = t;
            String info = "Tile: " + t.getType();
            if (t.getUnit() != null)
                info += " | Unit: " + t.getUnit().getName() + " (HP: " + t.getUnit().getHealth() + ")";
            if (t.getBuilding() != null)
                info += " | Building: " + t.getBuilding().getName() + " (" + t.getBuilding().getSymbol() + ")";
            view.displayMessage(info);
        }
    }

    private void moveUnit(Tile start, int x, int y) {
        Tile end = map.getTile(x, y);
        if (end == null || !end.getType().isAccessible() || end.isOccupied()) {
            view.displayMessage("Invalid move target.");
            return;
        }

        int dist = Math.abs(start.getX() - x) + Math.abs(start.getY() - y);
        if (dist > 1) { // Simple movement
            view.displayMessage("Too far!");
            return;
        }

        if (start.getUnit().getOwnerId() != player.getId()) {
            view.displayMessage("You cannot move enemy units!");
            return;
        }

        end.setUnit(start.getUnit());
        start.setUnit(null);
        selectedTile = end; // Follow unit
        view.displayMessage("Moved.");
    }

    private void attackUnit(Tile start, int x, int y) {
        Tile targetTile = map.getTile(x, y);
        if (targetTile == null || targetTile.getUnit() == null) {
            view.displayMessage("No target there.");
            return;
        }

        Unit attacker = start.getUnit();
        Unit defender = targetTile.getUnit();

        if (attacker.getOwnerId() == defender.getOwnerId()) {
            view.displayMessage("Friendly fire is not allowed!");
            return;
        }

        int dist = Math.abs(start.getX() - x) + Math.abs(start.getY() - y);
        if (dist > attacker.getRange()) {
            view.displayMessage("Target out of range!");
            return;
        }

        int dmg = Math.max(1, attacker.getAttack() - defender.getDefense());
        dmg += new Random().nextInt(3);

        defender.takeDamage(dmg);
        view.displayMessage("Dealt " + dmg + " damage. Target HP: " + defender.getHealth());

        if (!defender.isAlive()) {
            view.displayMessage(defender.getName() + " destroyed!");
            targetTile.setUnit(null);
            player.addResource(ResourceType.GOLD, 10);
        }
    }

    private void buildStructure(Tile tile, String type) {
        if (tile.isOccupied()) {
            view.displayMessage("Tile occupied.");
            return;
        }

        Building b = null;
        switch (type.toLowerCase()) {
            case "center":
                b = new CommandCenter(player.getId());
                break;
            case "camp":
                b = new TrainingCamp(player.getId());
                break;
            case "farm":
                b = new Farm(player.getId());
                break;
            case "mine":
                b = new Mine(player.getId());
                break;
            default:
                view.displayMessage("Unknown building: center, camp, farm, mine");
                return;
        }

        if (player.hasResource(ResourceType.GOLD, b.getCost())
                && player.hasResource(ResourceType.WOOD, b.getCost() / 2)) {
            player.consumeResource(ResourceType.GOLD, b.getCost());
            player.consumeResource(ResourceType.WOOD, b.getCost() / 2);
            tile.setBuilding(b);
            view.displayMessage("Built " + b.getName());
        } else {
            view.displayMessage("Not enough resources! Need " + b.getCost() + " Gold, " + (b.getCost() / 2) + " Wood.");
        }
    }

    private void recruitUnit(Tile tile, String type) {
        if (tile.getBuilding() == null) {
            view.displayMessage("No building here!");
            return;
        }

        boolean isCamp = tile.getBuilding() instanceof TrainingCamp;
        boolean isCenter = tile.getBuilding() instanceof CommandCenter;

        Unit u = null;
        switch (type.toLowerCase()) {
            case "soldier":
                u = new Soldier(player.getId());
                break;
            case "archer":
                if (!isCamp && !isCenter) {
                    view.displayMessage("Need Training Camp or Center!");
                    return;
                }
                u = new Archer(player.getId());
                break;
            case "cavalier":
                if (!isCamp) {
                    view.displayMessage("Need Training Camp!");
                    return;
                }
                u = new Cavalier(player.getId());
                break;
            default:
                view.displayMessage("Unknown unit: soldier, archer, cavalier");
                return;
        }

        if (u != null) {
            if (player.hasResource(ResourceType.GOLD, u.getCost()) && player.hasResource(ResourceType.FOOD, 20)) {
                Tile spawn = findEmptyAdjacent(tile);
                if (spawn != null) {
                    player.consumeResource(ResourceType.GOLD, u.getCost());
                    player.consumeResource(ResourceType.FOOD, 20);
                    spawn.setUnit(u);
                    view.displayMessage("Recruited " + u.getName());
                } else {
                    view.displayMessage("No space to spawn unit!");
                }
            } else {
                view.displayMessage("Not enough resources! Need " + u.getCost() + " Gold, 20 Food.");
            }
        }
    }

    private Tile findEmptyAdjacent(Tile center) {
        int[] dx = { 0, 0, 1, -1 };
        int[] dy = { 1, -1, 0, 0 };

        for (int i = 0; i < 4; i++) {
            Tile t = map.getTile(center.getX() + dx[i], center.getY() + dy[i]);
            if (t != null && !t.isOccupied() && t.getType().isAccessible())
                return t;
        }
        return null; // Try Diagonals?
    }

    private void endTurn() {
        view.displayMessage("Ending turn...");
        aiTurn();

        // Income
        int goldGain = 10; // Base
        int woodGain = 5;
        int stoneGain = 0;
        int foodGain = 5;

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Tile t = map.getTile(x, y);
                if (t.getBuilding() != null && t.getBuilding().getOwnerId() == player.getId()) {
                    Building b = t.getBuilding();
                    if (b instanceof Farm)
                        foodGain += 20;
                    else if (b instanceof Mine) {
                        stoneGain += 10;
                        goldGain += 10;
                    } else if (b instanceof CommandCenter) {
                        goldGain += 10;
                        woodGain += 5;
                    }
                }
            }
        }

        player.addResource(ResourceType.GOLD, goldGain);
        player.addResource(ResourceType.WOOD, woodGain);
        player.addResource(ResourceType.STONE, stoneGain);
        player.addResource(ResourceType.FOOD, foodGain);

        view.displayMessage("Income: +" + goldGain + "G, +" + woodGain + "W, +" + stoneGain + "S, +" + foodGain + "F");

        turnCount++;
        selectedTile = null;
    }

    private void aiTurn() {
        view.displayMessage("AI is thinking...");
        // Simple AI
        Tile target = null;
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Tile t = map.getTile(x, y);
                if (t.getBuilding() instanceof CommandCenter && t.getBuilding().getOwnerId() == player.getId()) {
                    target = t;
                    break;
                }
            }
        }

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Tile t = map.getTile(x, y);
                if (t.getUnit() != null && t.getUnit().getOwnerId() == enemy.getId()) {
                    Unit u = t.getUnit();
                    int moveX = x, moveY = y;
                    if (target != null) {
                        if (x < target.getX())
                            moveX++;
                        else if (x > target.getX())
                            moveX--;
                        else if (y < target.getY())
                            moveY++;
                        else if (y > target.getY())
                            moveY--;
                    } else {
                        moveX += new Random().nextInt(3) - 1;
                        moveY += new Random().nextInt(3) - 1;
                    }

                    Tile dest = map.getTile(moveX, moveY);
                    if (dest != null && !dest.isOccupied() && dest.getType().isAccessible()) {
                        dest.setUnit(u);
                        t.setUnit(null);
                    }
                }
            }
        }
    }
}
