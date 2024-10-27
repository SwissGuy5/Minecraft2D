package Objects;

import Graphics.TerrainRenderer;
import Terrain.*;

public class Player {
    boolean[] keysDown = new boolean[4];

    public int tempX;
    public int tempY;
    public double x;
    public double y;
    public double vx = 0;
    public double vy = 0;
    private final int maxV = 10;
    public int ax = 0;
    public int ay = 0;
    private final int maxA = -20;

    public boolean inWater = false;
    public boolean inAir = false;

    private Terrain terrain;
    // byte[][] tiles = new byte[64][128];
    // Chunk chunk1;
    // Chunk chunk2;
    // int tilesOffset;

    public int width = TerrainRenderer.TILE_SIZE - 4;
    public int height = TerrainRenderer.TILE_SIZE * 2 - 6;

    public Player(int x, int y, Terrain terrain) {
        this.x = x;
        this.y = y;
        this.tempX = x;
        this.tempY = y;
        this.terrain = terrain;
    }

    // private void updateColliders(){
    //     int playerPositionToChunk = this.getX() / 24;
    //     int pToQ = 0;
    //     for (int q = 0; q < 128; q++) {
    //         if ((q - 1) * 32 < playerPositionToChunk && playerPositionToChunk < q * 32) {
    //             pToQ = q / 2;
    //             break;
    //         }
    //     }
    //     tilesOffset = (pToQ - 1) * 32;
    //     for (int y = 0; y < 64; y++) {
    //         for (int x = 0; x < 128; x++) {
    //             if (x >= 64) {
    //                 tiles[y][x] = this.terrain.getChunk(pToQ).getTiles()[y][x - 64];
    //             } else {
    //                 tiles[y][x] = this.terrain.getChunk(pToQ - 1).getTiles()[y][x];
    //             }
    //         }
    //     } 
    // }

    // private byte getClosestBlockBelow() {
    //     int leftFoot = this.getX() / 24 - tilesOffset * 32;
    //     int rightFoot = (this.getX() + this.width) / 24 - tilesOffset * 32;
    //     int feetYChunk = -(this.getY() + this.height) / 24 + 32;
    //     for (int i = feetYChunk; i >= 0; i--) {
    //         if (tiles[i][leftFoot] != 0 || tiles[i][rightFoot] != 0) {
    //             return (byte)i;
    //         }
    //     }
    //     return 0;
    // }

    // private int[] getPlayerBlockPosition() {
    //     int blockX = this.getX() / 24 - tilesOffset * 32;
    //     int blockY = 32 - (this.getY() + this.height) / 24;
    //     return new int[]{blockX, blockY};
    // }

    // private boolean isGrounded() {
    //     // System.out.print(getPlayerBlockPosition()[1]);
    //     // System.out.print(" ");
    //     // System.out.println(getClosestBlockBelow());

    //     return false;
    // }

    // public int currChunk() {
    //     return this.getX() / Chunk.CHUNK_WIDTH;
    // }

    // public int chunkTile() {
    //     return this.getX() - currChunk() * Chunk.CHUNK_WIDTH;
    // }

    public int getX() {
        return getX(0);
    }
    private int getX(int tileOffset) {
        return (int)Math.floor(x + tileOffset);
    }

    public int getY() {
        return getY(0);
    }
    private int getY(int tileOffset) {
        return (int)Math.floor(y + tileOffset);
    }

    public int chunkOffset() {
        return chunkOffset(0);
    }
    private int chunkOffset(int tileOffset) {
        return (int)Math.floor((x + tileOffset) / Chunk.CHUNK_WIDTH);
    }

    public byte chunkX() {
        return chunkX(0);
    }
    private byte chunkX(int tileOffset) {
        return (byte)(this.getX(tileOffset) - chunkOffset(tileOffset) * Chunk.CHUNK_WIDTH);
    }

    public byte chunkY() {
        return chunkY(0);
    }
    private byte chunkY(int tileOffset) {
        return (byte)(y + tileOffset);
    }

    private void checkCollisions() {
        byte tile;
        boolean nonCollidingBlock = false;
        Chunk currChunk = terrain.getChunk(chunkOffset());

        // Floor Collision, let player through water
        tile = currChunk.getTile(chunkX(), chunkY(-1));
        for (int i = 0; i < Terrain.nonCollidingBlocks.length; i++) {
            if (tile == Terrain.nonCollidingBlocks[i]) {
                nonCollidingBlock = true;
            }
        }
        if (!nonCollidingBlock) {
            y = Math.ceil(y);
            inAir = false;
        }
        nonCollidingBlock = false;

        // // Left Wall Collision
        // for (int i = 0; i < Terrain.nonCollidingBlocks.length; i++) {
        //     if (currChunk.getTile(chunkX(-2), chunkY(-2)) == Terrain.nonCollidingBlocks[i] || currChunk.getTile(chunkX(-2), chunkY(-1)) == Terrain.nonCollidingBlocks[i]) {
        //         nonCollidingBlock = true;
        //     }
        // }
        // if (!nonCollidingBlock) {
        //     System.out.println(x);
        //     x = Math.floor(x + 1);
        // }
        // nonCollidingBlock = false;
        
        // Right Wall Collision
        // for (int i = 0; i < Terrain.nonCollidingBlocks.length; i++) {
        //     if (currChunk.getTile(chunkX(1), chunkY(-2)) == Terrain.nonCollidingBlocks[i] || currChunk.getTile(chunkX(1), chunkY(-1)) == Terrain.nonCollidingBlocks[i]) {
        //         nonCollidingBlock = true;
        //     }
        // }
        // if (!nonCollidingBlock) {
        //     System.out.println(x);
        //     x = Math.ceil(x - 1);
        // }
        // nonCollidingBlock = false;

        tile = currChunk.getTile(chunkX(), chunkY());
        if (tile == 85) {
            inWater = true;
        } else {
            inWater = false;
        }
    }

    public void update(double delta) {
        if (keysDown[1]) {
            vx = maxV;
        } else if (keysDown[3]) {
            vx = -maxV;
        } else {
            vx = 0;
        }
        // if (!inAir && keysDown[0]) {
        //     System.out.println(inAir);
        //     // ax = maxA;
        //     inAir = true;
        // } else {
        //     // ax = 0;
        // }
        if (keysDown[0]) {
            vy = maxV;
        } else if (keysDown[2]) {
            vy = -maxV;
        } else {
            vy = 0;
        }

        // vy = ay * delta;
        // if (vy > maxV) {
        //     vy = maxV;
        // }
        x += vx * delta;
        y += vy * delta;

        checkCollisions();

        // updateColliders();
        // isGrounded();
    }

    public void keyDown(int key) {
        this.keysDown[key] = true;
    }
    
    public void keyUp(int key) {
        this.keysDown[key] = false;
    }
}