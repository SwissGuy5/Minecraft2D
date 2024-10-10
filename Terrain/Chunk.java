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
    public int offset;
    private byte[][] tiles;
    
    public Chunk(double seed, int offset) {
        tiles = new byte[CHUNK_HEIGHT][CHUNK_WIDTH];
        this.offset = offset;
        
        double noise;
        for (byte x = 0; x < CHUNK_HEIGHT; x++) {
            noise = ((SimplexNoise.noise((((double)x + (offset * CHUNK_WIDTH) + .5) / 100), seed) + 1) / 2) * CHUNK_HEIGHT * .5 + CHUNK_HEIGHT * .25;
            noise *= Terrain.biomeNoise("plain");
            noise = Math.floor(noise);

            int dirtDepth = 4;
            int waterLevel = 27;
            for (byte y = 0; y < CHUNK_WIDTH; y++) {
                if (y > noise && y <= waterLevel) {
                    tiles[y][x] = 85;
                } else if (y > noise) {
                    tiles[y][x] = 0;
                } else if (y == noise && y >= waterLevel) {
                    tiles[y][x] = 9;
                } else if (y <= noise && y >= noise - dirtDepth) {
                    tiles[y][x] = 10;
                } else {
                    tiles[y][x] = 28;
                }
            }
        }
    }

    public byte getTile(byte x, byte y) {
        return tiles[y][x];
    }
}
