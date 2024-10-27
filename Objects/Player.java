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
    private final int maxVX = 7;
    // private final int maxVX = 50;
    private final int maxVY = 10;

    private final double ax = 15;
    private final double ay = -27;

    public boolean inWater = false;
    public boolean inAir = true;

    public Game game;
    private Terrain terrain;

    public int width = TerrainRenderer.TILE_SIZE - 4;
    public int height = TerrainRenderer.TILE_SIZE * 2 - 6;

    private int[] lightSources = new int[50 * 3];
    private int[] lightSourceIds = new int[50];

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
        Chunk currChunk = terrain.getChunk(chunkOffset());

        // Floor Collision, let player through water
        tile = currChunk.getTile(chunkX(), chunkY(-1));
        if (!Terrain.nonCollidingBlocks.contains(tile)) {
            y = Math.ceil(y);
            inAir = false;
        }

        // Wall Collision
        if (!Terrain.nonCollidingBlocks.contains(currChunk.getTile(chunkX(), chunkY(-1))) && !Terrain.nonCollidingBlocks.contains(currChunk.getTile(chunkX(), chunkY(-2)))) {
            x = Math.floor(x + 1);
        }
        if (!Terrain.nonCollidingBlocks.contains(currChunk.getTile(chunkX(1), chunkY(-1))) && !Terrain.nonCollidingBlocks.contains(currChunk.getTile(chunkX(1), chunkY(-2)))) {
            x = Math.floor(x);
        }
        
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

        // System.out.println(tile);
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
        } else if (y <= 1) {
            y = 1;
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
            for (int i = 0; i < lightSourceIds.length; i++) {
                boolean chunkEqual = this.lightSources[i * 3] == chunkNumber;
                boolean yEqual = this.lightSources[i * 3 + 1] == (byte)(chunkX - chunkNumber * Chunk.CHUNK_WIDTH);
                boolean xEqual = this.lightSources[i * 3 + 2] == (byte)chunkY;
                if (chunkEqual && yEqual && xEqual) {
                    this.game.renderer.lightingRenderer.removeLight(lightSourceIds[i]);
                }
            }
        }
    }

    void secondaryAction(int x, int y) {
        int chunkX = x / 48 + this.getX() - 600 / 48;
        byte chunkNumber = (byte)Math.floor((double)(chunkX) / Chunk.CHUNK_WIDTH);
        int chunkY = this.getY() - y / 48 + 400 / 48;
        byte tile = this.terrain.getTile(chunkNumber, (byte)(chunkX - chunkNumber * Chunk.CHUNK_WIDTH), (byte)chunkY);
        if (tile != 0) return;
        byte newTile = (byte)game.inventory.items[game.inventory.currentlySelected];
        this.terrain.updateTile(chunkNumber, (byte)(chunkX - chunkNumber * Chunk.CHUNK_WIDTH), (byte)chunkY, newTile);
        if (newTile == 90) {
            this.lightSources[0] = chunkNumber;
            this.lightSources[1] = (byte)(chunkX - chunkNumber * Chunk.CHUNK_WIDTH);
            this.lightSources[2] = (byte)chunkY;
            Light light = new Light(chunkX * 48, 2980 - chunkY * 48, 200);
            this.lightSourceIds[0] = light.id;
            this.game.renderer.lightingRenderer.addLight(light);
        }
    }
}