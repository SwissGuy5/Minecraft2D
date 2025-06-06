package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import Objects.Player;
import Terrain.Terrain;
import Terrain.Chunk;
import Objects.Game;


/**
 * The renderer for the terrain.
 */
public class TerrainRenderer extends JPanel {
    public static final int TILE_SIZE = 48;
    final Color sky = new Color(61, 211, 252);

    private Player player;
    private Terrain terrain;
    public static Map<Byte, BufferedImage> tileSprites = loadSprites();

    /**
     * Constructor of the terrain renderer.
     * @param game the game object.
     */
    public TerrainRenderer(Game game) {
        this.terrain = game.terrain;
        this.player = game.player;
        this.setBackground(sky);
        this.setVisible(true);
        this.setBounds(0, 0, Renderer.windowWidth, Renderer.windowHeight);
    }

    /**
     * Loads the sprites of the tiles.
     * @return a map of the tile sprites.
     */
    public static Map<Byte, BufferedImage> loadSprites() {
        Map<Byte, BufferedImage> tileSprites = new HashMap<>();
        String cwd = System.getProperty("user.dir");
        try {
            for (byte i = 1; i <= 91; i++) {
                String fileName = cwd + "\\Assets\\Tiles\\" + i + ".png";
                tileSprites.put(i, ImageIO.read(new File(fileName)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tileSprites;
    }

    /**
     * Draws a tile on the screen.
     * @param g the graphics object.
     * @param type the type of the tile.
     * @param x the x position of the tile.
     * @param y the y position of the tile.
     */
    void drawTile(Graphics g, byte type, double x, double y) {
        if (type == 0) {
            return;
        }
        BufferedImage sprite = TerrainRenderer.tileSprites.get(type);
        if (sprite != null) {
            g.drawImage(sprite, (int) Math.round(x * TILE_SIZE
                + (Renderer.windowWidth - player.width) / 2), (int) Math.floor(y * TILE_SIZE 
                + (Renderer.windowHeight - player.height) / 2) - 6, TILE_SIZE, TILE_SIZE, null);
        }
    }

    /**
     * Draws a chunk on the screen.
     * @param g the graphics object.
     * @param chunk the chunk to draw.
     */
    void drawChunk(Graphics g, Chunk chunk) {
        for (byte y = 0; y < Chunk.CHK_HGT; y++) {
            for (byte x = 0; x < Chunk.CHUNK_WIDTH; x++) {
                drawTile(g, chunk.getTile(x, y), 
                    chunk.offset * Chunk.CHUNK_WIDTH + x - player.x, player.y - y);
            }
        }
    }

    /**
     * Paints the nearest terrain on the screen.
     * @param g the graphics object.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int playerChunkOffset = player.chunkOffset();
        drawChunk(g, terrain.getChunk(playerChunkOffset - 1));
        drawChunk(g, terrain.getChunk(playerChunkOffset));
        drawChunk(g, terrain.getChunk(playerChunkOffset + 1));
    }
}
