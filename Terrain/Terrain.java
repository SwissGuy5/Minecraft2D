package Terrain;

public class Terrain {
    private Chunk[] chunks;
    public double seed;

    public Terrain() {
        this(Math.random());
    }
    public Terrain(double seed) {
        this.seed = seed;
        // System.out.println(seed * Math.pow(10, 17));
        chunks = new Chunk[100];
        generateChunks(0, 10);
        // FileHandler.EncodeJson(chunks);
    }

    private void generateChunks(int lower, int upper) {
        for (int i = lower; i < upper; i++) {
            this.addChunk(i);
        }
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
