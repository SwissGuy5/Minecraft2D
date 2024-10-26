package Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Objects.PlayerPanel;
import Objects.Game;
import Objects.Light;

public class Renderer extends JFrame {
    public MenuRenderer menuRenderer;
    private PlayerRenderer playerRenderer;
    private LightingRenderer lightingRenderer;
    private TerrainRenderer terrainRenderer;
    private PlayerPanel playerPanel;
    private Game game;
    static final int windowWidth = 1200;
    static final int windowHeight = 800;

    public Renderer(Game game) {
        super("Minecraft 2D");
        this.game = game;
        // this.getContentPane().setBackground(Color.GRAY);
        this.setSize(windowWidth, windowHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        menuRenderer = new MenuRenderer(game);
        menuRenderer.setBounds(0, 0, windowWidth, windowHeight);
        this.add(menuRenderer);
        
        this.setVisible(true);
    }

    public void init() {
        menuRenderer.setVisible(false);

        playerRenderer = new PlayerRenderer(game.player);
        playerRenderer.setBounds(500, 300, 200, 200);
        this.add(playerRenderer);

        lightingRenderer = new LightingRenderer(game);
        lightingRenderer.setBounds(0, 0, windowWidth, windowHeight);
        this.add(lightingRenderer);

        Light sun = new Light(500, 100, 2400);
        lightingRenderer.addLight(sun);

        // Light alsoSun = new Light(300, 200, 50);
        // lightingRenderer.addLight(alsoSun);

        terrainRenderer = new TerrainRenderer(game);
        terrainRenderer.setBounds(0, 0, windowWidth, windowHeight);
        this.add(terrainRenderer);

        playerPanel = new PlayerPanel(game.player);
        playerPanel.setBounds(0, 0, 0, 0);
        this.add(playerPanel);
        playerPanel.requestFocusInWindow();

        System.out.println("Initialised Renderer");
    }
}