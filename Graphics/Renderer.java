package Graphics;
import javax.swing.JFrame;

import Objects.Player;
import Terrain.Terrain;

public class Renderer extends JFrame {
    private TerrainRenderer terrainRenderer;
    private LightingRenderer lightingRenderer;
    private PlayerRenderer playerRenderer;
    final int windowWidth = 1200;
    final int windowHeight = 800;

    public Renderer(Terrain terrain, Player player) {
        super("Minecraft 2D");
        // this.getContentPane().setBackground(Color.GRAY);
        this.setSize(windowWidth, windowHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        playerRenderer = new PlayerRenderer(player);
        playerRenderer.setBounds(500, 300, 200, 200);
        this.add(playerRenderer);

        lightingRenderer = new LightingRenderer();
        lightingRenderer.setBounds(0, 0, windowWidth, windowHeight);
        this.add(lightingRenderer);

        terrainRenderer = new TerrainRenderer(terrain);
        terrainRenderer.setBounds(0, 0, windowWidth, windowHeight);
        this.add(terrainRenderer);
    }
}