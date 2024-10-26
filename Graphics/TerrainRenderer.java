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
import Objects.Game;


public class TerrainRenderer extends JPanel {
    final int TILE_SIZE = 12;
    final Color SKY = new Color(61, 211, 252);

    private Terrain terrain;
    private Map<Byte, BufferedImage> tileSprites;

    public TerrainRenderer(Game game) {
        this.terrain = game.terrain;
        this.setBackground(SKY);
        this.setVisible(true);
        this.setBounds(0, 0, Renderer.windowWidth, Renderer.windowHeight);
        loadSprites();
    }

    private void loadSprites() {
        tileSprites = new HashMap<>();
        String cwd = System.getProperty("user.dir");
        try {
            for (byte i = 1; i < (byte) 90; i++) {
                String fileName = cwd + "\\Assets\\Tiles\\" + i + ".png";
                tileSprites.put((byte) i, ImageIO.read(new File(fileName)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void drawTile(Graphics g, byte type, int x, int y) {
        if (type == 0) return;
        BufferedImage sprite = tileSprites.get(type);
        if (sprite != null) {
            g.drawImage(sprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        }
    }

    void drawChunk(Graphics g, Chunk chunk) {
        for (byte y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
            for (byte x = 0; x < Chunk.CHUNK_WIDTH; x++) {
                drawTile(g, chunk.getTile(x, y), x + chunk.offset * Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT - y - 1);
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Paints the rest of the component with the background color
        drawChunk(g, terrain.getChunk(0));
        drawChunk(g, terrain.getChunk(1));
    }

    public void update(Player player) {
        
    }
}
