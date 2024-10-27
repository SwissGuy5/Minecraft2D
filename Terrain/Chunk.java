package Terrain;

import Objects.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;

/**
 * The chunk object containing a list of tiles.
 */
public class Chunk {
    public static final byte CHUNK_WIDTH = 64;
    public static final byte CHK_HGT = 64;
    public int offset;
    private byte[][] tiles;

    public Rectangle[] obstacles;
    public boolean chunkModificationAccountedFor = false;

    private byte[] groundLevel = new byte[CHUNK_WIDTH];

    /**
     * Constructor for the Chunk object.
     * @param offset The offset of the chunk.
     * @param seed The seed for the chunk.
     */
    public Chunk(int offset, double seed) {
        this.offset = offset;
        tiles = new byte[CHK_HGT][CHUNK_WIDTH];
        
        ArrayList<Byte> treeRoots = new ArrayList<Byte>();
        double noise;
        double heightNoise;
        double mountainNoise;
        double humidityNoise;
        for (byte x = 0; x < CHUNK_WIDTH; x++) {
            heightNoise = ((SimplexNoise.noise((((double) x + (offset * CHUNK_WIDTH) + .5) / 50), 
                seed * 300) + 1) / 2) * CHK_HGT;
            mountainNoise = ((SimplexNoise.noise((((double) x + (offset * CHUNK_WIDTH) + .5) / 500),
                (seed + 1) * 300) + 1) / 2) * CHK_HGT;
            humidityNoise = ((SimplexNoise.noise((((double) x + (offset * CHUNK_WIDTH) + .5) / 500),
            (seed + 2) * 300) + 1) / 2);
            noise = heightNoise * .1 + mountainNoise * .6 + .2 * CHK_HGT;
            noise = Math.floor(noise);
            
            this.groundLevel[x] = (byte) noise;

            int dirtDepth;
            int waterLevel = 27;
            byte surfaceBlock = 9;;
            byte groundBlock = 10;

            if (humidityNoise > .7) {
                dirtDepth = 15;
                groundBlock = 32;
                surfaceBlock = 32;
            } else if (humidityNoise > .2) {
                dirtDepth = 6;
                surfaceBlock = 9;
            } else {
                dirtDepth = 3;
                surfaceBlock = 32;
            }

            for (byte y = 0; y < CHK_HGT; y++) {
                if (y == 0) {
                    tiles[y][x] = 16;
                } else if (y > noise && y <= waterLevel) {
                    tiles[y][x] = 85;
                } else if (y > noise) {
                    tiles[y][x] = 0;
                } else if (y == noise && y >= waterLevel) {
                    if (y < CHK_HGT - 10 && x >= 2 && x < CHUNK_WIDTH - 2 && Math.random() > .85) {
                        treeRoots.add((byte) (x));
                        treeRoots.add((byte) (y));
                        treeRoots.add((byte) (groundBlock));
                    }
                    tiles[y][x] = surfaceBlock;
                } else if (y <= noise && y >= noise - dirtDepth) {
                    tiles[y][x] = groundBlock;
                } else {
                    if (Math.random() > .98) {
                        tiles[y][x] = 24;
                    } else {
                        tiles[y][x] = 28;
                    }
                }
            }
        }

        byte prevX = -10;
        for (int i = 0; i < treeRoots.size(); i += 3) {
            byte x = treeRoots.get(i);
            byte y = treeRoots.get(i + 1);
            byte groundType = treeRoots.get(i + 2);
            if (x > prevX + 5) {
                generateTree(x, y, groundType);
                prevX = x;
            }
        }
    }

    /**
     * The constructor for the Chunk object.
     * @param offset The offset of the chunk.
     * @param tilesArr The JSONArray of tiles.
     */
    public Chunk(int offset, JSONArray tilesArr) {
        this.offset = offset;
        this.tiles = new byte[CHK_HGT][CHUNK_WIDTH];
        for (byte y = 0; y < CHK_HGT; y++) {
            JSONArray tilesXArr = (JSONArray) tilesArr.get(y);
            for (byte x = 0; x < CHUNK_WIDTH; x++) {
                tiles[y][x] = (byte) (long) tilesXArr.get(x);
            }
        }
    }

    private void generateTree(byte x, byte y, byte groundType) {
        Random randNum = new Random();
        int height = 3 + randNum.nextInt(3);
        
        if (groundType != 32) {
            tiles[y][x] = groundType;

            for (int i = 1; i <= height; i++) {
                tiles[y + i][x] = 2;
            }

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
        } else {
            for (int i = 0; i <= height - 2; i++) {
                tiles[y + i][x] = 91;
            }
            tiles[y + height - 2][x - 1] = 91;
            tiles[y + height - 1][x - 1] = 91;
            tiles[y + height - 2][x + 1] = 91;
            tiles[y + height - 1][x + 1] = 91;
            tiles[y + height][x + 1] = 91;
            tiles[y + height + 1][x + 1] = 91;
        }
    }

    public byte[][] getTiles() {
        return tiles;
    }
    
    /**
     * Returns the tile at the given coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The tile at the given coordinates.
     */
    public byte getTile(byte x, byte y) {
        if (x >= CHUNK_WIDTH) {
            return tiles[y][CHUNK_WIDTH - 1];
        }
        if (y >= CHK_HGT) {
            return tiles[CHK_HGT - 1][x];
        }
        return tiles[y][x];
    }
    
    /**
     * Sets the tile at the given coordinates to the given type.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param type The type of the tile.
     */
    public void setTile(byte x, byte y, byte type) {
        this.chunkModificationAccountedFor = false;
        tiles[y][x] = type;
    }

    /**
     * Saves the obstacles in the chunk.
     * @param obstacles The obstacles in the chunk.
     */
    public void saveObstacles(Rectangle[] obstacles) {
        this.obstacles = obstacles;
        this.chunkModificationAccountedFor = true;
    }
}
