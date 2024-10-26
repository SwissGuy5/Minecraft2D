package Objects;

import Graphics.Renderer;
import Terrain.Terrain;
import java.awt.EventQueue;

public class Game {
    public Terrain terrain;
    public Player player;
    private Renderer renderer;
    public Thread gameThread;

    // public Game() {
    //     // terrain = new Terrain(0.38464894137558436);
    //     // renderer = new Renderer(this);
    //     // this.init();

    //     // this.run();
    // }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void init() {
        terrain = new Terrain();
        player = new Player(10, 31);
        init2();
    }
    public void init(double seed) {
        terrain = new Terrain(seed);
        player = new Player(10, 31);
        init2();
    }
    private void init2() {
        renderer.init();
        gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Game.this.run();
            }
        });
        gameThread.start();
    }

    // Main game loop
    private void run() {
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
                // renderer.repaint();
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        renderer.repaint(Game.this);
                    }
                });
                try {
                    Thread.sleep(15);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // try {
            //     Thread.sleep(2);
            // } catch(InterruptedException e) {
            //     e.printStackTrace();
            // }
        }
    }
}