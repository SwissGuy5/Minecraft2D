package Objects;

import Terrain.*;

public class Player {
    boolean[] keysDown = new boolean[4];

    public int x;
    public int y;

    public int vx = 0;
    public int vy = 0;

    boolean setOnce = false;

    private Terrain terrain;
    byte[][] tiles = new byte[64][128];
    Chunk chunk1;
    Chunk chunk2;
    int tilesOffset;

    public int width = 30;
    public int height = 72;

    public Player(int x, int y, Terrain terrain) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
    }

    private void updateColliders(){ 
        int playerPositionToChunk = this.x / 24;
        int pToQ = 0;
        for (int q = 0; q < 128; q++) {
            if ((q - 1) * 32 < playerPositionToChunk && playerPositionToChunk < q * 32) {
                pToQ = q / 2;
                break;
            }
        }
        tilesOffset = (pToQ - 1) * 32;
        for (int y = 0; y < 64; y++) {
            for (int x = 0; x < 128; x++) {
                if (x >= 64) {
                    tiles[y][x] = this.terrain.getChunk(pToQ).getTiles()[y][x - 64];
                } else {
                    tiles[y][x] = this.terrain.getChunk(pToQ - 1).getTiles()[y][x];
                }
            }
        } 
    }

    private byte getClosestBlockBelow() {
        int leftFoot = this.x / 24 - tilesOffset * 32;
        int rightFoot = (this.x + this.width) / 24 - tilesOffset * 32;
        int feetYChunk = -(this.y + this.height) / 24 + 32;
        for (int i = feetYChunk; i >= 0; i--) {
            if (tiles[i][leftFoot] != 0 || tiles[i][rightFoot] != 0) {
                return (byte)i;
            }
        }
        return 0;
    }

    private int[] getPlayerBlockPosition() {
        int blockX = this.x / 24 - tilesOffset * 32;
        int blockY = 32 - (this.y + this.height) / 24;
        return new int[]{blockX, blockY};
    }

    private boolean isGrounded() {
        // System.out.print(getPlayerBlockPosition()[1]);
        // System.out.print(" ");
        // System.out.println(getClosestBlockBelow());

        return false;
    }

    public void update(int delta) {
        if (keysDown[1]) {
            vx = 1;
        } else if (keysDown[3]) {
            vx = -1;
        } else {
            vx = 0;
        }
        if (keysDown[0]) {
            vy = -1;
        } else if (keysDown[2]) {
            vy = 1;
        } else {
            vy = 0;
        }

        this.x += vx * delta;
        this.y += vy * delta;

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