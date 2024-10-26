package Objects;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

public class PlayerPanel extends JPanel implements KeyListener {
    private Player player;

    public PlayerPanel(Player player) {
        this.player = player;
        this.setFocusable(true);
        this.addKeyListener(this);
        this.requestFocusInWindow();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
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
        int key = e.getKeyCode();
        switch (key) {
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
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
