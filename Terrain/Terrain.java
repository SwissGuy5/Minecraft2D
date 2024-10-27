package Terrain;

import java.util.HashMap;

import Objects.Light;
import Objects.Rectangle;
import java.util.ArrayList;

public class Terrain {
    private HashMap<Integer, Chunk> chunks;
    public double seed;
    public static ArrayList<Byte> nonCollidingBlocks = getNonCollidingBlocks();

    private Rectangle[] obstacles;

    public Terrain() {
        this(Math.random());
    }
    public Terrain(double seed) {
        this.seed = seed;

        if (FileHandler.fileExists(seed)) {
            System.out.println("Getting existing terrain");
            chunks = FileHandler.getChunksFromSeed(seed);
        } else {
            System.out.println("Generating Terrain");
            chunks = new HashMap<Integer, Chunk>();
            generateChunks(0, 20);
            // Todo: Re-enable to save files
        }
    }

    private void generateChunks(int lower, int upper) {
        for (int i = lower; i < upper; i++) {
            this.addChunk(i);
        }
    }

    public void updateTile(int offset, byte x, byte y, byte type) {
        chunks.get(offset).setTile(x, y, type);
    }

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

    public static double biomeNoise(String biome, double noise) {
        noise = noise * .5 * Chunk.CHUNK_HEIGHT + .25 * Chunk.CHUNK_HEIGHT;
        switch (biome) {
            case "plain":
                return noise * 1/5 + 25;
            case "coast":
                return noise * 1/6 + 20;
            case "ocean":
                return noise * 1/6 + 20;
            default:
                return noise;
        }
        // double noise = SimplexNoise.noise((((double)x + offset + .5) / 70), seed);
        // return (( + 1) / 2) * CHUNK_HEIGHT * .5 + CHUNK_HEIGHT * .25;
    }

    public Chunk getChunk(int n) {
        Chunk currChunk = chunks.get(n);
        if (currChunk == null) {
            this.addChunk(n);
            return chunks.get(n);
        }
        return currChunk;
    }

    public void addChunk(int offset) {
        chunks.put(offset, new Chunk(offset, this.seed));
    }

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
                if (tiles[y][x] != 0) {
                    if (y > 0 && tiles[y - 1][x] == 0) {
                        if (x > 0 && tiles[y][x - 1] == 0) {
                            int endX = x;
                            while (tiles[y][endX] != 0 && endX < tiles[0].length) {
                                endX++;
                            }
                            int endY = y;
                            while (tiles[endY][x] != 0 && endY < tiles.length) {
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
                            Rectangle rectangle = new Rectangle(new int[]{x1, y1, x2, y2, x3, y3, x4, y4});
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
