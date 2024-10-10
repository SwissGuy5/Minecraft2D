package Terrain;

/* 
 * MOST COMMON BLOCKS:
 *  - dirt with grass on top: 9
 *  - dirt without grass: 10
 *  - rock: 13
 */

public class Chunk {
    public static final byte CHUNK_WIDTH = 64;
    public static final byte CHUNK_HEIGHT = 64;
    private byte[][] tiles;
    
    public Chunk() {
        // double noise = SimplexNoise.noise(4.3, 2.7);
        // System.out.println(noise);
        tiles = new byte[CHUNK_HEIGHT][CHUNK_WIDTH];
        for (byte y = 0; y < CHUNK_HEIGHT; y++) {
            for (byte x = 0; x < CHUNK_WIDTH; x++) {
                if (y >= 30 && x > y) {
                    if (x - 1 > y) {
                        tiles[y][x] = 10;
                    } else {
                        tiles[y][x] = 9;
                    }
                } else if (y == 30) {
                    tiles[y][x] = 9;
                } else if (y < 30) {
                    tiles[y][x] = 13;
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
