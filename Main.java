import Graphics.Renderer;
import Terrain.Terrain;

public class Main {
    private Terrain terrain;
    private Character character;
    private Renderer renderer;
    private Thread loopThread;

    Main() {
        terrain = new Terrain();
        terrain.addChunk(0);

        character = new Character(10, 31);

        renderer = new Renderer(terrain);
        renderer.setVisible(true);

        this.update();
    }

    // Main game loop
    void update() {
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
        // game.init();
        // game.render(n);
    }
}