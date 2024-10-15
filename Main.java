import Graphics.Renderer;
import Player.Player;
import Terrain.Terrain;

public class Main {
    private Terrain terrain;
    private Player player;
    private Renderer renderer;

    Main() {
        terrain = new Terrain();
        terrain.addChunk(0);
        terrain.addChunk(1);

        player = new Player(10, 31);

        renderer = new Renderer(terrain, player);
        renderer.setVisible(true);

        this.run();
    }

    // Main game loop
    void run() {
        final int FPS = 60;
        final double TIME_PER_FRAME = 1000000000 / FPS;
        long prevTime = System.nanoTime();
        double delta = 0;

        while (true) {
            long currTime = System.nanoTime();
            delta += (currTime - prevTime) / TIME_PER_FRAME;
            prevTime = currTime;

            if (delta >= 1) {
                delta--;
                renderer.repaint();
            }
        }
    }

    public static void main(String[] args) {
        Main game = new Main();
    }
}