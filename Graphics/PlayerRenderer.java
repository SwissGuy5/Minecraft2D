package Graphics;
import Player.Player;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import Terrain.*;

public class PlayerRenderer extends JPanel {

    private Player player;

    public PlayerRenderer(Player player) {
        this.player = player;
        this.setOpaque(false);
    }

    void drawCharacter(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, 200, 200);

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(200 / 2, 200 / 2);
        g2d.rotate(Math.PI / 3);
        g2d.translate(-100 / 2, -100 / 2);
        g2d.setColor(new Color(255, 0, 0));
        g2d.fillRect(0, 0, 100, 100);
    }

    public void paintComponent(Graphics g) {
        // super.paintComponent(g);
        // drawCharacter(g);
    }
}
