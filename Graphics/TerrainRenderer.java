package Graphics;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Objects.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import Terrain.*;
import Objects.Player;
import Objects.Game;


public class TerrainRenderer extends JPanel {
    public static final int TILE_SIZE = 48;
    final Color SKY = new Color(61, 211, 252);

    private Player player;
    private Terrain terrain;
    static public Map<Byte, BufferedImage> tileSprites = loadSprites();

    public TerrainRenderer(Game game) {
        this.terrain = game.terrain;
        this.player = game.player;
        this.setBackground(SKY);
        this.setVisible(true);
        this.setBounds(0, 0, Renderer.windowWidth, Renderer.windowHeight);
    }

    static public Map<Byte, BufferedImage> loadSprites() {
        Map<Byte, BufferedImage> tileSprites = new HashMap<>();
        String cwd = System.getProperty("user.dir");
        try {
            for (byte i = 1; i < 90; i++) {
                String fileName = cwd + "\\Assets\\Tiles\\" + i + ".png";
                tileSprites.put(i, ImageIO.read(new File(fileName)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tileSprites;
    }

    void drawTile(Graphics g, byte type, double x, double y) {
        if (type == 0) return;
        BufferedImage sprite = TerrainRenderer.tileSprites.get(type);
        if (sprite != null) {
            g.drawImage(sprite, (int)Math.round(x * TILE_SIZE + (Renderer.windowWidth - player.width) / 2), (int)Math.floor(y * TILE_SIZE + (Renderer.windowHeight - player.height) / 2) - 6, TILE_SIZE, TILE_SIZE, null);
        }
    }

    void drawChunk(Graphics g, Chunk chunk) {
        for (byte y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
            // System.out.println(Chunk.CHUNK_HEIGHT - player.getY() + y);
            for (byte x = 0; x < Chunk.CHUNK_WIDTH; x++) {
                drawTile(g, chunk.getTile(x, y), chunk.offset * Chunk.CHUNK_WIDTH + x - player.x, player.y - y);
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int playerChunkOffset = player.chunkOffset();
        drawChunk(g, terrain.getChunk(playerChunkOffset - 1));
        drawChunk(g, terrain.getChunk(playerChunkOffset));
        drawChunk(g, terrain.getChunk(playerChunkOffset + 1));
    }
}
