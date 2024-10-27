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

public class Renderer extends JFrame {
    static final int windowWidth = 1200;
    static final int windowHeight = 800;

    private InventoryRenderer inventoryRenderer;
    private PlayerRenderer playerRenderer;
    private LightingRenderer lightingRenderer;
    private TerrainRenderer terrainRenderer;
    // private PlayerPanel playerPanel;
    private Game game;
    private MenuRenderer menuRenderer;

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
            // this.addComponentListener(new ComponentAdapter() {
            //     public void componentResized(ComponentEvent componentEvent) {
            //         System.out.println("Resizing");
            //     }
            // });
            
            menuRenderer = new MenuRenderer(game);
            this.add(menuRenderer);
            
            this.setVisible(true);
    }

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
    public void repaint(double delta) {
        // terrainRenderer.update(game.player);
        game.player.update(delta);
        super.repaint();
    }

    // @Override
    // public void windowClosing(WindowEvent e) {
    //     this.super(e);
    // } 
}