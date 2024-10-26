package Objects;

import Graphics.Renderer;
import Terrain.Terrain;
import java.awt.EventQueue;
import java.awt.event.*;
import java.util.HashMap;

public class Game implements KeyListener{
    private Renderer renderer;
    public Terrain terrain;
    public Player player;
    public Inventory inventory;
    public Thread gameThread;
    public HashMap<Integer, Boolean> keysDown = new HashMap<Integer, Boolean>();

    public void init() {
        terrain = new Terrain();
        player = new Player(-100, -600);
        init2();
    }
    public void init(double seed) {
        terrain = new Terrain(seed);
        player = new Player(-100, -600);
        init2();
    }
    private void init2() {
        inventory = new Inventory();
        renderer.init();
        gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Game.this.run();
            }
        });
        gameThread.start();
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
        renderer.addKeyListener(this);
        renderer.setFocusable(true);
        renderer.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        keysDown.put(e.getKeyCode(), true);
        System.out.println(e.getKeyCode());
        handleInput(e.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        keysDown.put(e.getKeyCode(), false);
    }

    private void handleInput(int keyCode) {
        if (keyCode >= 49 || keyCode <= 57) {
            inventory.setSelected(keyCode);
        }
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