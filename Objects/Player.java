package Objects;

import Graphics.TerrainRenderer;
import Terrain.Chunk;
import Terrain.Terrain;

/**
 * The player object that the user controls.
 */
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
    public boolean inAir = true;

    public Game game;
    private Terrain terrain;

    public int width = TerrainRenderer.TILE_SIZE - 4;
    public int height = TerrainRenderer.TILE_SIZE * 2 - 6;

    private int[] lightSources = new int[50 * 3];
    private int[] lightSourceIds = new int[50];

    /**
     * Constructor of the player object.
     * @param x initial x position of the player.
     * @param y initial y position of the player.
     * @param game the game object.
     */
    public Player(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.tempX = x;
        this.tempY = y;
        this.game = game;
        this.terrain = game.terrain;
    }

    /**
     * Returns the x position of the player.
     * @return the x position of the player.
     */
    public int getX() {
        return getX(0);
    }

    private int getX(int tileOffset) {
        return (int) Math.floor(x + tileOffset);
    }

    /**
     * Returns the y position of the player.
     * @return the y position of the player.
     */
    public int getY() {
        return getY(0);
    }

    private int getY(int tileOffset) {
        return (int) Math.floor(y + tileOffset);
    }

    /**
     * Returns the chunk the player is currently in.
     * @return the chunk offset of the player.
     */
    public int chunkOffset() {
        return chunkOffset(0);
    }

    private int chunkOffset(int tileOffset) {
        return (int) Math.floor((x + tileOffset) / Chunk.CHUNK_WIDTH);
    }

    /**
     * Returns the x position of the player within the chunk.
     * @return the x position of the player within the chunk.
     */
    public byte chunkX() {
        return chunkX(0);
    }

    private byte chunkX(int tileOffset) {
        return (byte) (this.getX(tileOffset) - chunkOffset(tileOffset) * Chunk.CHUNK_WIDTH);
    }

    /**
     * Returns the y position of the player within the chunk.
     * @return the y position of the player within the chunk.
     */
    public byte chunkY() {
        return chunkY(0);
    }

    private byte chunkY(int tileOffset) {
        return (byte) (y + tileOffset);
    }

    /**
     * Checks for collisions with the terrain.
     * @param offsetX integer offset in the x direction.
     * @param offsetY integer offset in the y direction.
     * @return true if there is a collision, false otherwise.
     */
    private boolean checkCollision(int offsetX, int offsetY) {
        Chunk chunk;
        Chunk prevChunk = terrain.getChunk(chunkOffset() - 1);
        Chunk currChunk = terrain.getChunk(chunkOffset());
        Chunk nextChunk = terrain.getChunk(chunkOffset() + 1);

        if (chunkX() + offsetX < 0) {
            chunk = prevChunk;
        } else if (chunkX() + offsetX < Chunk.CHUNK_WIDTH) {
            chunk = currChunk;
        } else {
            chunk = nextChunk;
        }
        if (!Terrain.nonCollidingBlocks.contains(chunk.getTile(chunkX(offsetX), chunkY(offsetY)))) {
            return true;
        }
        return false;
    }

    /**
     * Checks for collisions with the terrain and updates the player's position.
     */
    private void checkCollisions() {
        if (checkCollision(0, -1) || checkCollision(1, -1)) {
            y = Math.ceil(y);
            inAir = false;
        } else {
            inAir = true;
        }

        // Wall Collision
        if (checkCollision(0, -1) || checkCollision(0, 0)) {
            x = Math.floor(x + 1);
        }
        if (checkCollision(1, -1) || checkCollision(1, 0)) {
            x = Math.floor(x);
        }

        // Head collision
        if (checkCollision(0, 1) || checkCollision(1, 1)) {
            y = Math.ceil(y - .5);
        }

        byte tile = terrain.getChunk(chunkOffset()).getTile(chunkX(), chunkY());
        if (tile == 85) {
            inWater = true;
        } else {
            inWater = false;
        }
    }

    /**
     * Updates the player's position based on the keys pressed.
     * @param delta the time since the last update.
     */
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

    /**
     * Handles the primary action of the player. This is used for breaking blocks.
     * It breaks the block and if the block is a torch, it removes the light source.
     * @param x the x position of the mouse.
     * @param y the y position of the mouse.
     */
    public void primaryAction(int x, int y) {
        int chunkX = x / 48 + this.getX() - 600 / 48;
        byte chunkNumber = (byte) Math.floor((double) (chunkX) / Chunk.CHUNK_WIDTH);
        int chunkY = this.getY() - y / 48 + 400 / 48;
        byte tile = this.terrain.getTile(chunkNumber, 
            (byte) (chunkX - chunkNumber * Chunk.CHUNK_WIDTH), (byte) chunkY);
        if (tile == 16) {
            return;
        }
        this.terrain.updateTile(chunkNumber, (byte)
            (chunkX - chunkNumber * Chunk.CHUNK_WIDTH), (byte) chunkY, (byte) 0);
        if (tile == 90) {
            for (int i = 0; i < lightSourceIds.length; i++) {
                boolean chunkEqual = this.lightSources[i * 3] == chunkNumber;
                boolean yEqual = this.lightSources[i * 3 + 1]
                    == (byte) (chunkX - chunkNumber * Chunk.CHUNK_WIDTH);
                boolean xEqual = this.lightSources[i * 3 + 2] == (byte) chunkY;
                if (chunkEqual && yEqual && xEqual) {
                    this.game.renderer.lightingRenderer.removeLight(lightSourceIds[i]);
                }
            }
        }
    }

    /**
     * Handles the secondary action of the player. This is used for placing blocks.
     * It places the block and if the block is a torch, it adds a light source.
     * @param x the x position of the mouse.
     * @param y the y position of the mouse.
     */
    public void secondaryAction(int x, int y) {
        int chunkX = x / 48 + this.getX() - 600 / 48;
        byte chunkNumber = (byte) Math.floor((double) (chunkX) / Chunk.CHUNK_WIDTH);
        int chunkY = this.getY() - y / 48 + 400 / 48;
        byte tile = this.terrain.getTile(chunkNumber, 
            (byte) (chunkX - chunkNumber * Chunk.CHUNK_WIDTH), (byte) chunkY);
        if (tile != 0) {
            return;
        }
        byte newTile = (byte) game.inventory.items[game.inventory.currentlySelected];
        this.terrain.updateTile(chunkNumber, 
            (byte) (chunkX - chunkNumber * Chunk.CHUNK_WIDTH), (byte) chunkY, newTile);
        if (newTile == 90) {
            this.lightSources[0] = chunkNumber;
            this.lightSources[1] = (byte) (chunkX - chunkNumber * Chunk.CHUNK_WIDTH);
            this.lightSources[2] = (byte) chunkY;
            Light light = new Light(chunkX * 48, 2980 - chunkY * 48, 200);
            this.lightSourceIds[0] = light.id;
            this.game.renderer.lightingRenderer.addLight(light);
        }
    }
}