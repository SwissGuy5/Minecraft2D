package Terrain;

import org.json.simple.JSONArray;

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
    
    public Chunk(int offset, double seed) {
        this.offset = offset;
        tiles = new byte[CHUNK_HEIGHT][CHUNK_WIDTH];
        
        double noise;
        for (byte x = 0; x < CHUNK_WIDTH; x++) {
            noise = ((SimplexNoise.noise((((double)x + (offset * CHUNK_WIDTH) + .5) / 100), seed * 100) + 1) / 2) * CHUNK_HEIGHT * .5 + CHUNK_HEIGHT * .25;
            noise *= Terrain.biomeNoise("plain");
            noise = Math.floor(noise);

            int dirtDepth = 4;
            int waterLevel = 27;
            for (byte y = 0; y < CHUNK_HEIGHT; y++) {
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
             
        if (offset == 0) {
            tiles[50][10] = 5;
            tiles[50][11] = 5;
            tiles[51][10] = 5;
            tiles[51][11] = 5;
        }
    }
    public Chunk(int offset, JSONArray tilesArr) {
        this.offset = offset;
        this.tiles = new byte[CHUNK_HEIGHT][CHUNK_WIDTH];
        for (byte y = 0; y < CHUNK_HEIGHT; y++) {
            JSONArray tilesXArr = (JSONArray)tilesArr.get(y);
            for (byte x = 0; x < CHUNK_WIDTH; x++) {
                tiles[y][x] = (byte)(long)tilesXArr.get(x);
            }
        }
    }

    public byte[][] getTiles() {
        return tiles;
    }

    public byte getTile(byte x, byte y) {
        return tiles[y][x];
    }
}
