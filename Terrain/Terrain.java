package Terrain;

public class Terrain {
    private Chunk[] chunks;

    public Terrain() {
        chunks = new Chunk[100];
    }

    public Chunk getChunk(int n) {
        return chunks[n];
    }

    public void addChunk(int n) {
        chunks[n] = new Chunk();
    }
}
