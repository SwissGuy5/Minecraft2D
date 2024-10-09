import javax.swing.*;

import Graphics.Renderer;
import Terrain.Terrain;

public class Main {
    private Terrain terrain;

    Main() {
        terrain = new Terrain();
        terrain.addChunk(0);

        Renderer renderer = new Renderer(terrain);
        renderer.setVisible(true);
    }

    public static void main(String[] args) {
        Main game = new Main();
        // game.init();
        // game.render(n);
    }
}