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
    // public HashMap<Integer, Boolean> keysDown = new HashMap<Integer, Boolean>();

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
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 49 && keyCode <= 57) {
            inventory.setSelected(keyCode);
        }

        switch (keyCode) {
            case KeyEvent.VK_UP:
                player.keyDown(0);
                break;
            case KeyEvent.VK_DOWN:
                player.keyDown(2);
                break;
            case KeyEvent.VK_LEFT:
                player.keyDown(3);
                break;
            case KeyEvent.VK_RIGHT:
                player.keyDown(1);
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                player.keyUp(0);
                break;
            case KeyEvent.VK_DOWN:
                player.keyUp(2);
                break;
            case KeyEvent.VK_LEFT:
                player.keyUp(3);
                break;
            case KeyEvent.VK_RIGHT:
                player.keyUp(1);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

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