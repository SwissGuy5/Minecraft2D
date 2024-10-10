package Graphics;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import Terrain.*;


public class TerrainRenderer extends JPanel {
    final int TILE_SIZE = 12;
    final Color SKY = new Color(61, 211, 252);
    final Color DIRT = new Color(140, 113, 104);
    final Color STONE = new Color(97, 92, 90);
    final Color WOOD = new Color(87, 60, 43);
    final Color WATER = new Color(28, 129, 212);

    private Terrain cachedTerrain;
    private Map<Byte, BufferedImage> tileSprites;

    public TerrainRenderer(Terrain cachedTerrain) {
        this.cachedTerrain = cachedTerrain;
        this.setBackground(SKY);
        loadSprites();
    }

    private void loadSprites() {
        tileSprites = new HashMap<>();
        try {
        for (byte i = 0; i < (byte) 89; i++) {
            String fileName = "./Assets/Tiles/texture-" + i + ".png";
            tileSprites.put((byte) i, ImageIO.read(getClass().getResource(fileName)));
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void drawTile(Graphics g, byte type, byte x, byte y) {
        BufferedImage sprite = tileSprites.get(type);
        if (sprite != null) {
            g.drawImage(sprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        }
    }

    void drawChunk(Graphics g, Chunk chunk) {
        for (byte y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
            for (byte x = 0; x < Chunk.CHUNK_WIDTH; x++) {
                drawTile(g, chunk.getTile(x, y), x, (byte) (Chunk.CHUNK_HEIGHT - y - 1));
            }
        }
    }

    public void paintComponent(Graphics g) {
        // super.paintComponent(g); // Paints the rest of the component with the background color
        drawChunk(g, cachedTerrain.getChunk(0));
    }
}
