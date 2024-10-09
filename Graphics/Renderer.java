package Graphics;
import javax.swing.*;
import java.awt.*;
import Terrain.Terrain;

public class Renderer extends JFrame {
    private TerrainRenderer terrainRenderer;

    public Renderer(Terrain terrain) {
        super("Minecraft 2D");
        // this.getContentPane().setBackground(Color.GRAY);
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        terrainRenderer = new TerrainRenderer(terrain);
        this.add(terrainRenderer);
    }

    // drawWorld() {
    //     terrainRenderer.paint(getGraphics());
    // }
}
