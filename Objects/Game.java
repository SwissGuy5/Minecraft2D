package Objects;

import Graphics.Renderer;
import Terrain.Terrain;
import java.awt.EventQueue;
import java.awt.event.*;
import java.util.HashMap;

public class Game implements KeyListener {
    public Renderer renderer;
    public Terrain terrain;
    public Player player;
    public Inventory inventory;
    public Thread gameThread;
    public double delta;
    // public HashMap<Integer, Boolean> keysDown = new HashMap<Integer, Boolean>();

    private MouseInput mouseInput;

    public void init() {
        terrain = new Terrain();
        player = new Player(2, 50, this);
        init2();
    }
    public void init(double seed) {
        terrain = new Terrain(seed);
        player = new Player(-2, 50, this);
        init2();
        // terrain.updateTile(0, (byte)0, (byte)0, (byte)0);
    }
    private void init2() {
        inventory = new Inventory();
        mouseInput = new MouseInput(this);
        renderer.addMouseListener(mouseInput);

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
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
                player.keyDown(0);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                player.keyDown(2);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                player.keyDown(3);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                player.keyDown(1);
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
                player.keyUp(0);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                player.keyUp(2);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                player.keyUp(3);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                player.keyUp(1);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // Main game loop
    // private void run() {
    //     final int FPS = 60;
    //     final double TIME_PER_FRAME = 1000000000 / FPS;
    //     long prevTime = System.nanoTime();
    //     double delta = 0;
        
    //     while (true) {
    //         long currTime = System.nanoTime();
    //         delta += (currTime - prevTime) / TIME_PER_FRAME;
    //         prevTime = currTime;

    //         if (delta >= 1) {
    //             delta--;
    //             // player.update(delta);
    //             // player.update(delta * TIME_PER_FRAME);
    //             EventQueue.invokeLater(new Runnable() {
    //                 @Override
    //                 public void run() {
    //                     renderer.repaint(Game.this);
    //                 }
    //             });
    //             try {
    //                 Thread.sleep(15);
    //             } catch(InterruptedException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }
    // }
    private void run() {
        final int FPS = 60;
        final double nanoToSecond = 1000000000;
        final double TIME_PER_FRAME = 1 / FPS;
        long prevTime = System.nanoTime();
        delta = 0;
        
        while (true) {
            long currTime = System.nanoTime();
            delta = (currTime - prevTime) / nanoToSecond;
            
            if (delta >= TIME_PER_FRAME) {                
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        renderer.repaint(Game.this.delta);
                    }
                });

                prevTime = currTime;
                delta -= TIME_PER_FRAME;
                
                try {
                    Thread.sleep(15);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class MouseInput implements MouseListener {
    private Game game;

    public MouseInput(Game game) {
        this.game = game;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            game.player.primaryAction(e.getX(), e.getY());
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            game.player.secondaryAction(e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}