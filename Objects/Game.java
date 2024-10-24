package Objects;

import Graphics.Renderer;
import Terrain.Terrain;

public class Game {
    public Terrain terrain;
    public Player player;
    private Renderer renderer;

    public Game() {
        terrain = new Terrain(0.38464894137558436);
        player = new Player(10, 31);
        renderer = new Renderer(this);

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
}