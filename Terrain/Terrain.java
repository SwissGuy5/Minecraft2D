package Terrain;

import java.util.HashMap;

import Objects.Light;
import Objects.Rectangle;
import java.util.ArrayList;

public class Terrain {
    private HashMap<Integer, Chunk> chunks;
    public double seed;

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
            generateChunks(0, 10);
            // Todo: Re-enable to save files
            // saveTerrain();
        }
    }

    private void generateChunks(int lower, int upper) {
        for (int i = lower; i < upper; i++) {
            this.addChunk(i);
        }
    }

    public void saveTerrain() {
        FileHandler.saveChunksWithSeed(seed, chunks);
    }

    public static double biomeNoise(String biome) {
        // double noise = SimplexNoise.noise((((double)x + offset + .5) / 70), seed);
        // return (( + 1) / 2) * CHUNK_HEIGHT * .5 + CHUNK_HEIGHT * .25;
        return 1d;
    }

    public Chunk getChunk(int n) {
        return chunks.get(n);
    }

    public ArrayList<Rectangle> getLightCollisionRectangles(int n) {
        ArrayList<Rectangle> rectangles = new ArrayList<>();

        Chunk chunk = this.chunks.get(n);
        int offset = chunk.offset * 64;
        byte[][] tiles = chunk.getTiles();

        for (int x = 0; x < tiles[0].length; x++) {
            for (int y = 0; y < tiles.length; y++) {
                if (tiles[y][x] != 0) {
                    if (y > 0 && tiles[y - 1][x] == 0) {
                        if (x > 0 && tiles[y][x - 1] == 0) {
                            int endX = x;
                            while (tiles[y][endX] != 0) {
                                endX++;
                            }
                            int endY = y;
                            while (tiles[endY][x] != 0) {
                                endY++;
                            }
                            Rectangle rectangle = new Rectangle(new int[]{12 * (offset + x), 64 * 12 - 12 * y, 12 * (offset + endX), 64 * 12 - 12 * y, 12 * (offset + x), 64 * 12 - 12 * endY, 12 * (offset + endX), 64 * 12 - 12 * endY});
                            rectangles.add(rectangle);
                        }
                    }
                }
            }
        }

        return rectangles;
    }

    public void addChunk(int offset) {
        chunks.put(offset, new Chunk(offset, this.seed));
    }
}
