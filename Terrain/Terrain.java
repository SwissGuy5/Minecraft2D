package Terrain;

public class Terrain {
    private Chunk[] chunks;
    public double seed;

    public Terrain() {
        chunks = new Chunk[100];
        seed = Math.random() * 100;
    }

    public static double biomeNoise(String biome) {
        // double noise = SimplexNoise.noise((((double)x + offset + .5) / 70), seed);
        // return (( + 1) / 2) * CHUNK_HEIGHT * .5 + CHUNK_HEIGHT * .25;
        return 1d;
    }

    public Chunk getChunk(int n) {
        return chunks[n];
    }

    public void addChunk(int offset) {
        chunks[offset] = new Chunk(this.seed, offset);
    }
}
