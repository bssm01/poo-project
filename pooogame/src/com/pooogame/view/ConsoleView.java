package com.pooogame.view;

import com.pooogame.model.entities.Unit;
import com.pooogame.model.entities.Building;
import com.pooogame.model.map.GameMap;
import com.pooogame.model.map.Tile;
import com.pooogame.model.player.Player;
import com.pooogame.model.ResourceType;

import java.util.Scanner;

public class ConsoleView {
    private Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMap(GameMap map) {
        System.out.println("   " + getColumnNumbers(map.getWidth()));

        for (int y = 0; y < map.getHeight(); y++) {
            System.out.printf("%2d ", y); // Row number
            for (int x = 0; x < map.getWidth(); x++) {
                Tile tile = map.getTile(x, y);
                String symbol = tile.getType().getSymbol();

                if (tile.getUnit() != null) {
                    Unit u = tile.getUnit();
                    // If player 1 (ID 0) lowercase, Enemy (ID 1) uppercase or coloring
                    // We'll just use the symbol for now. Only P1 has units initially.
                    // Let's mark P1 units with [] and P2 with {} maybe?
                    if (u.getOwnerId() == 0)
                        symbol = "[" + u.getSymbol() + "]";
                    else
                        symbol = "{" + u.getSymbol() + "}";
                } else if (tile.getBuilding() != null) {
                    Building b = tile.getBuilding();
                    if (b.getOwnerId() == 0)
                        symbol = "[" + b.getSymbol() + "]";
                    else
                        symbol = "{" + b.getSymbol() + "}";
                } else {
                    symbol = " " + symbol + " ";
                }

                System.out.print(symbol + " ");
            }
            System.out.println();
        }
    }

    private String getColumnNumbers(int width) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) {
            sb.append(String.format(" %d  ", i));
        }
        return sb.toString();
    }

    public void displayPlayerInfo(Player player) {
        System.out.println("=== Player: " + player.getName() + " ===");
        for (ResourceType type : ResourceType.values()) {
            System.out.print(type + ": " + player.getResources().getOrDefault(type, 0) + " | ");
        }
        System.out.println();
    }

    public void displayMessage(String msg) {
        System.out.println(">> " + msg);
    }

    public String prompt(String message) {
        System.out.print(message + " ");
        return scanner.nextLine();
    }
}
