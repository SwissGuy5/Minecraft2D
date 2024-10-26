package Graphics;
import javax.swing.JFrame;

import Objects.Game;
import Objects.Light;

public class Renderer extends JFrame {
    private InventoryRenderer inventoryRenderer;
    public MenuRenderer menuRenderer;
    private PlayerRenderer playerRenderer;
    private LightingRenderer lightingRenderer;
    private TerrainRenderer terrainRenderer;
    private Game game;
    static final int windowWidth = 1200;
    static final int windowHeight = 800;

    public Renderer(Game game) {
        super("Minecraft 2D");
        this.game = game;
        this.setSize(windowWidth, windowHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        menuRenderer = new MenuRenderer(game);
        this.add(menuRenderer);
        
        this.setVisible(true);
    }

    public void init() {
        System.out.println("Initialising Renderer");
        menuRenderer.setVisible(false);

        InventoryRenderer inventoryRenderer = new InventoryRenderer();
        this.add(inventoryRenderer);

        // Todo: Re-enable once working
        // playerRenderer = new PlayerRenderer(game.player);
        // playerRenderer.setBounds(500, 300, 200, 200);
        // this.add(playerRenderer);

        lightingRenderer = new LightingRenderer(game);
        lightingRenderer.setBounds(0, 0, windowWidth, windowHeight);
        this.add(lightingRenderer);

        Light sun = new Light(500, 100, 2400);
        lightingRenderer.addLight(sun);

        // Light alsoSun = new Light(300, 200, 50);
        // lightingRenderer.addLight(alsoSun);

        terrainRenderer = new TerrainRenderer(game);
        this.add(terrainRenderer);
    }

    @Override
    public void repaint() {
        super.repaint();
    }
    public void repaint(Game game) {
        terrainRenderer.update(game.player);
        super.repaint();
    }
}