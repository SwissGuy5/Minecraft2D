package Graphics;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Objects.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import Terrain.*;

public class PlayerRenderer extends JPanel {
    private Player player;

    double velocity = 1;

    double rightLegProgress = 0;
    double leftLegProgress = 0;

    int x = 0;
    int y = 0;

    public PlayerRenderer(Player player) {
        this.player = player;
        this.setOpaque(false);
    }

    void handleAnimation() {
        
    }

    void drawCharacter(Graphics g) {
        // DEBUG
        // g.setColor(new Color(0, 0, 0));
        // g.fillRect(0, 0, 200, 200);

        // THIS IS WHERE THE MAGIC HAPPENS
        Graphics2D g2d = (Graphics2D) g;

        // DRAWING THE HEAD
        g2d.setColor(new Color(255, 255, 0));
        g2d.fillRect(75, 20, 50, 40);

        // ALLOWING ROTATIONS
        AffineTransform originalTransform = g2d.getTransform();

        // DRAWING THE RIGHT LEG
        g2d.translate(110, 120);
        g2d.rotate(this.rightLegProgress);
        g2d.setColor(new Color(0, 0, 255));
        g2d.fillRect(-10, 0, 20, 90);

        g2d.setTransform(originalTransform);

        // DRAWING THE LEFT LEG
        g2d.translate(90, 120);
        g2d.rotate(this.leftLegProgress);
        g2d.setColor(new Color(0, 0, 255));
        g2d.fillRect(-10, 0, 20, 90);

        g2d.setTransform(originalTransform);
        
        // DRAWING THE TORSO
        g2d.setColor(new Color(0, 150, 0));
        g2d.fillRect(80, 60, 40, 50);
        g2d.setColor(new Color(0, 0, 255));
        g2d.fillRect(80, 110, 40, 20);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        handleAnimation();
        drawCharacter(g);
    }
}
