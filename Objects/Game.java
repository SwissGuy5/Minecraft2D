package Objects;

import Graphics.Renderer;
import Terrain.Terrain;
import java.awt.EventQueue;
import java.awt.event.*;

/**
 * The main game object that contains all the other objects.
 */
public class Game implements KeyListener {
    public Renderer renderer;
    public Terrain terrain;
    public Player player;
    public Inventory inventory;
    public Thread gameThread;
    public double delta;

    private MouseInput mouseInput;

    /**
     * Initialization method of the game object without a seed.
     */
    public void init() {
        terrain = new Terrain();
        player = new Player(0, 63, this);
        init2();
    }

    /**
     * Initialization method of the game object with a seed.
     */
    public void init(double seed) {
        terrain = new Terrain(seed);
        player = new Player(0, 63, this);
        init2();
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

    /**
     * Sets the renderer of the game object.
     * @param renderer the renderer object.
     */
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
            default:
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
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void run() {
        final int FPS = 60;
        final double nanoToSecond = 1000000000;
        final double TIME_PER_F = 1 / FPS;
        long prevTime = System.nanoTime();
        delta = 0;
        
        while (true) {
            long currTime = System.nanoTime();
            delta = (currTime - prevTime) / nanoToSecond;
            
            if (delta >= TIME_PER_F) {                
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        renderer.repaint(Game.this.delta);
                    }
                });

                prevTime = currTime;
                delta -= TIME_PER_F;
                
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
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