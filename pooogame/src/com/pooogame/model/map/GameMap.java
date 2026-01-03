package com.pooogame.model.map;

import java.util.Random;

public class GameMap {
    private int width;
    private int height;
    private Tile[][] grid;
    private Random random;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[height][width];
        this.random = new Random();
        generateMap();
    }

    private void generateMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TileType type = TileType.GRASS;
                double roll = random.nextDouble();
                if (roll < 0.1) {
                    type = TileType.MOUNTAIN;
                } else if (roll < 0.2) {
                    type = TileType.WATER;
                }
                grid[y][x] = new Tile(x, y, type);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x];
        }
        return null; // or throw exception
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
