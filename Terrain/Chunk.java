package Terrain;

public class Chunk {
    public static final byte CHUNK_WIDTH = 64;
    public static final byte CHUNK_HEIGHT = 64;
    private byte[][] tiles;
    
    public Chunk() {
        tiles = new byte[CHUNK_HEIGHT][CHUNK_WIDTH];
        for (byte y = 0; y < CHUNK_HEIGHT; y++) {
            for (byte x = 0; x < CHUNK_WIDTH; x++) {
                if (y > 30) {
                    if (x - 1 > y) {
                        tiles[y][x] = 7;
                    } else if (x > y) {
                        tiles[y][x] = 6;
                    }
                } else if (y <= 30) {
                    tiles[y][x] = 47;
                } else {
                    tiles[y][x] = 0;
                }
            }
        }
    }

    public byte getTile(byte x, byte y) {
        return tiles[y][x];
    }
}
