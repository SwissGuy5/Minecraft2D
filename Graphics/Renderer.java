package Graphics;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import Objects.Game;
import Objects.Light;

/**
 * The main renderer frame containing all the sub-renderers.
 */
public class Renderer extends JFrame {
    static int windowWidth = 1200;
    static int windowHeight = 800;

    private InventoryRenderer inventoryRenderer;
    private PlayerRenderer playerRenderer;
    public LightingRenderer lightingRenderer;
    private TerrainRenderer terrainRenderer;
    private Game game;
    private MenuRenderer menuRenderer;

    /**
     * Constructor for the Renderer class.
     * @param game The game object.
     */
    public Renderer(Game game) {
        super("Minecraft 2D");
        this.game = game;
        this.setSize(windowWidth, windowHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (game.terrain != null && JOptionPane.showConfirmDialog(
                    Renderer.this, "Save before quitting?", "Quit Game",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
                    ) == JOptionPane.YES_OPTION) {
                        game.terrain.saveTerrain();
                        // System.exit(0);
                }
            }
        });
        
        menuRenderer = new MenuRenderer(game);
        this.add(menuRenderer);
        
        this.setVisible(true);
    }

    /**
     * Initialises the renderer.
     */
    public void init() {
        System.out.println("Initialising Renderer");
        menuRenderer.setVisible(false);

        inventoryRenderer = new InventoryRenderer(game.inventory);
        this.add(inventoryRenderer);

        playerRenderer = new PlayerRenderer(game.player);
        this.add(playerRenderer);

        lightingRenderer = new LightingRenderer(game);
        this.add(lightingRenderer);

        terrainRenderer = new TerrainRenderer(game);
        this.add(terrainRenderer);

        System.out.println("Initialised Renderer");
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    /**
     * Repaints the renderer.
     * @param delta The time since the last repaint.
     */
    public void repaint(double delta) {
        game.player.update(delta);
        super.repaint();
    }
}