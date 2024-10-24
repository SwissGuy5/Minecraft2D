package Graphics;
import javax.swing.JFrame;

import Objects.Game;
import Objects.Light;

public class Renderer extends JFrame {
    private TerrainRenderer terrainRenderer;
    private LightingRenderer lightingRenderer;
    private PlayerRenderer playerRenderer;
    final int windowWidth = 1200;
    final int windowHeight = 800;

    public Renderer(Game game) {
        super("Minecraft 2D");
        // this.getContentPane().setBackground(Color.GRAY);
        this.setSize(windowWidth, windowHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // // Todo: Re-enable once working
        // playerRenderer = new PlayerRenderer(game.player);
        // // playerRenderer.setBounds(500, 300, 200, 200);
        // // this.add(playerRenderer);

        lightingRenderer = new LightingRenderer(game.terrain);
        lightingRenderer.setBounds(0, 0, windowWidth, windowHeight);
        this.add(lightingRenderer);

        Light sun = new Light(500, 50, 1000);
        lightingRenderer.addLight(sun);

        // Light alsoSun = new Light(300, 200, 500);
        // lightingRenderer.addLight(alsoSun);

        terrainRenderer = new TerrainRenderer(game.terrain);
        terrainRenderer.setBounds(0, 0, windowWidth, windowHeight);
        this.add(terrainRenderer);

        this.setVisible(true);
    }
}