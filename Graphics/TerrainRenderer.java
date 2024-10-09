package Graphics;
import javax.swing.*;
import java.awt.*;
import Terrain.*;

public class TerrainRenderer extends JPanel {
    final int TILE_SIZE = 12;
    final Color SKY = new Color(61, 211, 252);
    final Color DIRT = new Color(140, 113, 104);
    final Color STONE = new Color(97, 92, 90);
    final Color WOOD = new Color(87, 60, 43);
    final Color WATER = new Color(28, 129, 212);

    private Terrain cachedTerrain;

    public TerrainRenderer(Terrain cachedTerrain) {
        this.cachedTerrain = cachedTerrain;

        this.setBackground(SKY);
    }

    void drawTile(Graphics g, byte type, byte x, byte y) {
        Graphics2D g2 = (Graphics2D) g;

        switch (type) {
            case 0:
                g2.setColor(SKY);
                break;
            case 1:
                g2.setColor(DIRT);
                break;
            case 2:
                g2.setColor(STONE);
                break;
            case 3:
                g2.setColor(WATER);
                break;
            default:
                return;
        }
        g2.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    void drawChunk(Graphics g, Chunk chunk) {
        for (byte y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
            // System.out.println(Chunk.CHUNK_HEIGHT - y);
            for (byte x = 0; x < Chunk.CHUNK_WIDTH; x++) {
                drawTile(g, chunk.getTile(x, y), x, (byte)(Chunk.CHUNK_HEIGHT - y - 1));
            }
        }
    }

    public void paintComponent(Graphics g) {
        // super.paintComponent(g); // Paints the rest of the component with the background color
        drawChunk(g, cachedTerrain.getChunk(0));
    }
}
