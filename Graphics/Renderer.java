package Graphics;
import javax.swing.JFrame;

import Objects.Game;

public class Renderer extends JFrame {
    private TerrainRenderer terrainRenderer;
    private PlayerRenderer playerRenderer;
    final int windowWidth = 1200;
    final int windowHeight = 800;

    public Renderer(Game game) {
        super("Minecraft 2D");
        // this.getContentPane().setBackground(Color.GRAY);
        this.setSize(windowWidth, windowHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        playerRenderer = new PlayerRenderer(game.player);
        playerRenderer.setBounds(500, 300, 200, 200);
        this.add(playerRenderer);

        terrainRenderer = new TerrainRenderer(game.terrain);
        terrainRenderer.setBounds(0, 0, windowWidth, windowHeight);
        this.add(terrainRenderer);

        this.setVisible(true);
    }
}