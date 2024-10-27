package Terrain;

import org.json.simple.JSONArray;

import Objects.Rectangle;

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

    public Rectangle[] obstacles;
    public boolean chunkModificationAccountedFor = false;

    private byte[] groundLevel = new byte[CHUNK_WIDTH];
    
    public Chunk(int offset, double seed) {
        this.offset = offset;
        tiles = new byte[CHUNK_HEIGHT][CHUNK_WIDTH];
        
        ArrayList<Byte> treeRoots = new ArrayList<Byte>();
        double noise;
        double heightNoise;
        double mountainNoise;
        for (byte x = 0; x < CHUNK_WIDTH; x++) {
            // noise = ((SimplexNoise.noise((((double)x + (offset * CHUNK_WIDTH) + .5) / 100), seed * 100) + 1) / 2) * CHUNK_HEIGHT * .5 + CHUNK_HEIGHT * .25;
            // noise = ((SimplexNoise.noise((((double)x + (offset * CHUNK_WIDTH) + .5) / 100), seed * 100) + 1) / 2);
            // noise = Terrain.biomeNoise("", noise);
            
            heightNoise = ((SimplexNoise.noise((((double)x + (offset * CHUNK_WIDTH) + .5) / 50), seed * 300) + 1) / 2) * CHUNK_HEIGHT;
            mountainNoise = ((SimplexNoise.noise((((double)x + (offset * CHUNK_WIDTH) + .5) / 500), (seed + 1) * 300) + 1) / 2) * CHUNK_HEIGHT;
            noise = heightNoise * .1 + mountainNoise * .6 + .2 * CHUNK_HEIGHT;
            noise = Math.floor(noise);
            
            this.groundLevel[x] = (byte)noise;

            int dirtDepth = 4;
            int waterLevel = 27;
            for (byte y = 0; y < CHUNK_HEIGHT; y++) {
                if (y == 0) {
                    tiles[y][x] = 59;
                } else if (y > noise && y <= waterLevel) {
                    tiles[y][x] = 85;
                } else if (y > noise) {
                    tiles[y][x] = 0;
                }else if (y == noise && y >= waterLevel) {
                    // Add trees
                    if (y < CHUNK_HEIGHT - 10 && x >= 2 && x < CHUNK_WIDTH - 2 && Math.random() > .85) {
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

        byte prevX = -10;
        for (int i = 0; i < treeRoots.size(); i += 2) {
            byte x = treeRoots.get(i);
            byte y = treeRoots.get(i + 1);
            if (x > prevX + 5) {
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

        // Trunk
        int height = 3 + randNum.nextInt(3);
        for (int i = 1; i <= height; i++) {
            tiles[y + i][x] = 2;
        }
 
        // Leaves
        tiles[y + height + 1][x - 2] = 89;
        tiles[y + height + 1][x - 1] = 89;
        tiles[y + height + 1][x] = 89;
        tiles[y + height + 1][x + 1] = 89;
        tiles[y + height + 1][x + 2] = 89;
        tiles[y + height + 2][x - 2] = 89;
        tiles[y + height + 2][x - 1] = 89;
        tiles[y + height + 2][x] = 89;
        tiles[y + height + 2][x + 1] = 89;
        tiles[y + height + 2][x + 2] = 89;
        tiles[y + height + 3][x - 1] = 89;
        tiles[y + height + 3][x] = 89;
        tiles[y + height + 3][x + 1] = 89;
        tiles[y + height + 4][x - 1] = 89;
        tiles[y + height + 4][x] = 89;
        tiles[y + height + 4][x + 1] = 89;
    }

    public byte[][] getTiles() {
        return tiles;
    }
    
    public byte getTile(byte x, byte y) {
        return tiles[y][x];
    }
    
    public void setTile(byte x, byte y, byte type) {
        tiles[y][x] = type;
    }

    public int isUnderGroundAt(int x) {
        return this.groundLevel[x];
    }

    public void saveObstacles(Rectangle[] obstacles) {
        this.obstacles = obstacles;
        this.chunkModificationAccountedFor = true;
    }
}
