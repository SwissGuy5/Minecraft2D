package Terrain;

import java.util.HashMap;

import Objects.Rectangle;
import java.util.ArrayList;

/**
 * The Terrain class is responsible for generating and storing the terrain of the game.
 */
public class Terrain {
    private HashMap<Integer, Chunk> chunks;
    public double seed;
    public static ArrayList<Byte> nonCollidingBlocks = getNonCollidingBlocks();

    private Rectangle[] obstacles;

    public Terrain() {
        this(Math.random());
    }

    /**
     * Creating the terrain with a given seed.
     * @param seed The seed for the terrain generation.
     */
    public Terrain(double seed) {
        this.seed = seed;

        if (FileHandler.fileExists(seed)) {
            System.out.println("Getting existing terrain");
            chunks = FileHandler.getChunksFromSeed(seed);
        } else {
            System.out.println("Generating Terrain");
            chunks = new HashMap<Integer, Chunk>();
            generateChunks(0, 20);
        }
    }

    private void generateChunks(int lower, int upper) {
        for (int i = lower; i < upper; i++) {
            this.addChunk(i);
        }
    }

    /**
     * Updates the tile at the given coordinates.
     * @param offset The offset of the chunk.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @param type The type of the tile.
     */
    public void updateTile(int offset, byte x, byte y, byte type) {
        chunks.get(offset).setTile(x, y, type);
    }

    /**
     * Gets the tile at the given coordinates.
     * @param offset The offset of the chunk.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     */
    public byte getTile(int offset, byte x, byte y) {
        return chunks.get(offset).getTile(x, y);
    }

    /**
     * Saves the terrain to a file.
     */
    public void saveTerrain() {
        System.out.println("Saving");
        FileHandler.saveChunksWithSeed(seed, chunks);
    }

    private static ArrayList<Byte> getNonCollidingBlocks() {
        ArrayList<Byte> blocks = new ArrayList<Byte>();
        blocks.add((byte)0);
        blocks.add((byte)2);
        blocks.add((byte)85);
        blocks.add((byte)90);
        blocks.add((byte)91);
        return blocks;
    }

    /**
     * Calculates the terrain.
     * @param biome The curent biome.
     * @param noise The noise value.
     * @return The height of the terrain.
     */
    public static double biomeNoise(String biome, double noise) {
        noise = noise * .5 * Chunk.CHK_HGT + .25 * Chunk.CHK_HGT;
        switch (biome) {
            case "plain":
                return noise * 1 / 5 + 25;
            case "coast":
                return noise * 1 / 6 + 20;
            case "ocean":
                return noise * 1 / 6 + 20;
            default:
                return noise;
        }
    }

    /**
     * Gets the chunk at the given offset.
     * @param n The offset of the chunk.
     * @return The chunk at the given offset.
     */
    public Chunk getChunk(int n) {
        Chunk currChunk = chunks.get(n);
        if (currChunk == null) {
            this.addChunk(n);
            return chunks.get(n);
        }
        return currChunk;
    }

    /**
     * Adds a chunk at the given offset.
     * @param offset The offset of the chunk.
     */
    public void addChunk(int offset) {
        chunks.put(offset, new Chunk(offset, this.seed));
    }

    /**
     * Gets the light collision rectangles.
     * @param n The offset of the chunk.
     * @return The light collision rectangles.
     */
    public Rectangle[] getLightCollisionRectangles(int n) {
        ArrayList<Rectangle> rectangles = new ArrayList<>();

        Chunk chunk = this.chunks.get(n);

        if (chunk.chunkModificationAccountedFor) {
            return chunk.obstacles;
        }

        int offset = n * 64;
        byte[][] tiles = chunk.getTiles();

        int x1;
        int y1;
        int x2;
        int y2;
        int x3;
        int y3;
        int x4;
        int y4;

        for (int x = 0; x < tiles[0].length; x++) {
            for (int y = 0; y < tiles.length; y++) {
                if (!nonCollidingBlocks.contains(tiles[y][x])) {
                    if (y > 0 && nonCollidingBlocks.contains(tiles[y - 1][x])) {
                        if (x > 0 && nonCollidingBlocks.contains(tiles[y][x - 1])) {
                            int endX = x;
                            while (endX < 64 && !nonCollidingBlocks.contains(tiles[y][endX]) 
                                && endX < tiles[0].length) {
                                endX++;
                            }
                            int endY = y;
                            while (!nonCollidingBlocks.contains(tiles[endY][x]) 
                                && endY < tiles.length) {
                                endY++;
                            }
                            x1 = 48 * (offset + x);
                            y1 = 48 * (64 - y);
                            x2 = 48 * (offset + endX);
                            y2 = 48 * (64 - y);
                            x3 = 48 * (offset + x);
                            y3 = 48 * (64 - endY);
                            x4 = 48 * (offset + endX);
                            y4 = 48 * (64 - endY);
                            Rectangle rectangle = 
                                new Rectangle(new int[]{x1, y1, x2, y2, x3, y3, x4, y4});
                            rectangles.add(rectangle);
                        }
                    }
                }
            }
        }

        chunk.saveObstacles(rectangles.toArray(new Rectangle[rectangles.size()]));

        return this.obstacles;
    }
}
