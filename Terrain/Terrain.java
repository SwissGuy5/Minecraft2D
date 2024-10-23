package Terrain;

import java.util.HashMap;

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

    public void addChunk(int offset) {
        chunks.put(offset, new Chunk(offset, this.seed));
    }
}
