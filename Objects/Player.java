package Objects;

import Graphics.Renderer;
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
    private final int maxVX = 7;
    private final int maxVY = 10;

    private final double ax = 15;
    private final double ay = -27;

    public boolean inWater = false;
    public boolean inAir = false;

    public Game game;
    private Terrain terrain;

    public int width = TerrainRenderer.TILE_SIZE - 4;
    public int height = TerrainRenderer.TILE_SIZE * 2 - 6;

    private int[] lightSources = new int[5 * 2];

    public Player(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.tempX = x;
        this.tempY = y;
        this.game = game;
        this.terrain = game.terrain;
    }

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
            if (vx < 0) {
                vx += 3 * ax * delta;
            } else {
                vx += ax * delta;
            }
        } else if (keysDown[3]) {
            if (vx > 0) {
                vx += -3 * ax * delta;
            } else {
                vx += -ax * delta;
            }
        } else {
            if (vx > 0) {
                vx += -2 * ax * delta;
                if (vx < 0) {
                    vx = 0;
                }
            } else if (vx < 0) {
                vx += 2 * ax * delta;
                if (vx > 0) {
                    vx = 0;
                }
            }
        }
        if (vx > maxVX) {
            vx = maxVX;
        }
        if (vx < -maxVX) {
            vx = -maxVX;
        }

        if (keysDown[0] && inWater) {
            vy = maxVY / 2;
        } else if (inWater) {
            vy += .2 * ay * delta;
        } else if (inAir) {
            vy += ay * delta;
        } else if (keysDown[0]) {
            inAir = true;
            vy = maxVY;
        }

        x += vx * delta;
        y += vy * delta;

        if (y >= Chunk.CHUNK_HEIGHT) {
            y = Chunk.CHUNK_HEIGHT - 1;
        }

        checkCollisions();
    }

    public void keyDown(int key) {
        this.keysDown[key] = true;
    }
    
    public void keyUp(int key) {
        this.keysDown[key] = false;
    }

    void primaryAction(int x, int y) {
        int chunkX = x / 48 + this.getX() - 600 / 48;
        byte chunkNumber = (byte)Math.floor((double)(chunkX) / Chunk.CHUNK_WIDTH);
        int chunkY = this.getY() - y / 48 + 400 / 48;
        byte tile = this.terrain.getTile(chunkNumber, (byte)(chunkX - chunkNumber * Chunk.CHUNK_WIDTH), (byte)chunkY);
        if (tile == 59) return;
        this.terrain.updateTile(chunkNumber, (byte)(chunkX - chunkNumber * Chunk.CHUNK_WIDTH), (byte)chunkY, (byte)0);
        if (tile == 90) {
            // remove a light source
        }
    }

    void secondaryAction(int x, int y) {
        int chunkX = x / 48 + this.getX() - 600 / 48;
        byte chunkNumber = (byte)Math.floor((double)(chunkX) / Chunk.CHUNK_WIDTH);
        int chunkY = this.getY() - y / 48 + 400 / 48;
        byte tile = this.terrain.getTile(chunkNumber, (byte)(chunkX - chunkNumber * Chunk.CHUNK_WIDTH), (byte)chunkY);
        if (tile != 0) return;
        this.terrain.updateTile(chunkNumber, (byte)(chunkX - chunkNumber * Chunk.CHUNK_WIDTH), (byte)chunkY, (byte)game.inventory.items[game.inventory.currentlySelected]);
        if (tile == 90) {
            // add a light source
        }
    }
}