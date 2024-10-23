package Terrain;

import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.util.Random;

/* 
 * MOST COMMON BLOCKS:
 *  - dirt with grass on top: 9
 *  - dirt without grass: 10
 *  - rock: 13
 *  - trunk: 2 (temp)
*/

public class Chunk {
    public static final byte CHUNK_WIDTH = 64;
    public static final byte CHUNK_HEIGHT = 64;
    public int offset;
    private byte[][] tiles;
    
    public Chunk(int offset, double seed) {
        this.offset = offset;
        tiles = new byte[CHUNK_HEIGHT][CHUNK_WIDTH];
        
        ArrayList<Byte> treeRoots = new ArrayList<Byte>();
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
                }else if (y == noise && y >= waterLevel) {
                    if (y <= CHUNK_HEIGHT - 6 && x >= 1 && x < CHUNK_WIDTH - 1 && Math.random() > .8) {
                        treeRoots.add((byte)(x));
                        treeRoots.add((byte)(y));
                    }
                    tiles[y][x] = 9;
                } else if (y <= noise && y >= noise - dirtDepth) {
                    tiles[y][x] = 10;
                } else {
                    tiles[y][x] = 28;
                }
            }
        }

        byte prevX = -5;
        for (int i = 0; i < treeRoots.size(); i += 2) {
            byte x = treeRoots.get(i);
            byte y = treeRoots.get(i + 1);
            if (x > prevX + 3) {
                generateTree(x, y);
                prevX = x;
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

    private void generateTree(byte x, byte y) {
        tiles[y][x] = 10;
        Random randNum = new Random();

        int height = 3 + randNum.nextInt(3);
        for (int i = 1; i <= height; i++) {
            tiles[y + i][x] = 2;
        }
        // byte height = 3 + Math.round(Math.random() * 2);
        // byte height = 3 + (Math.floor(Math.random() * 3);

        // double randNum = Math.random();
        // if (randNum > .2) {
        //     tiles[y + 4][x] = 2;
        // } if (randNum > .85) {
        //     tiles[y + 5][x] = 2;
        // }

        tiles[y + height + 2][x] = 89;
        tiles[y + height + 1][x] = 89;
        tiles[y + height + 1][x - 1] = 89;
        tiles[y + height + 1][x + 1] = 89;
    }

    public byte[][] getTiles() {
        return tiles;
    }

    public byte getTile(byte x, byte y) {
        return tiles[y][x];
    }
}
