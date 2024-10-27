package Graphics;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.json.simple.parser.ContentHandler;

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

    double animationProgress = 0;
    int animationDirection = 1;

    public PlayerRenderer(Player player) {
        this.setOpaque(false);
        this.setBounds((Renderer.windowWidth - player.width) / 2 - 20, (Renderer.windowHeight - player.height) / 2, player.width + 40, player.height);
        this.player = player;
    }

    void handleAnimation() {
        if (player.vx == 0) {
            animationProgress = 0;
        } else {
            animationProgress += Math.abs(player.vx) * 0.01 * animationDirection;
            if (animationProgress > Math.PI / 4) {
                animationDirection = -1;
            } else if (animationProgress < -Math.PI / 4) {
                animationDirection = 1;
            }
        }
    }

    void drawCharacter(Graphics g) {
        // THIS IS WHERE THE MAGIC HAPPENS
        Graphics2D g2d = (Graphics2D) g;

        Color hairColor = new Color(110, 70, 41);
        Color skinColor = new Color(229, 194, 152);
        Color shirtColor = new Color(117, 144, 112);
        Color sleeveColor = new Color(112, 159, 100);
        Color pantsColor = new Color(36, 38, 47);

        // DRAWING THE HEAD
        g2d.setColor(skinColor);
        g2d.fillRect(30, 0, 24, 24);

        // DRAWING THE HAIR
        g2d.setColor(hairColor);
        g2d.fillRect(30, 0, 24, 4);
        g2d.fillRect(30, 0, 4, 10);

        // ALLOWING ROTATIONS
        AffineTransform originalTransform = g2d.getTransform();

        // DRAWING THE RIGHT ARM
        g2d.translate(42, 34);
        g2d.rotate(-this.animationProgress);
        g2d.setColor(sleeveColor);
        g2d.fillRect(-5, 0, 10, 10);
        g2d.setColor(skinColor);
        g2d.fillRect(-5, 10, 10, 20);

        g2d.setTransform(originalTransform);

        // DRAWING THE RIGHT LEG
        g2d.translate(42, 60);
        g2d.rotate(this.animationProgress);
        g2d.setColor(pantsColor);
        g2d.fillRect(-10, 0, 20, 20);
        g2d.setColor(skinColor);
        g2d.fillRect(-10, 20, 20, 10);

        g2d.setTransform(originalTransform);

        // DRAWING THE LEFT LEG
        g2d.translate(42, 60);
        g2d.rotate(-this.animationProgress);
        g2d.setColor(pantsColor);
        g2d.fillRect(-10, 0, 20, 20);
        g2d.setColor(skinColor);
        g2d.fillRect(-10, 20, 20, 10);

        g2d.setTransform(originalTransform);
        
        // DRAWING THE TORSO
        g2d.setColor(shirtColor);
        g2d.fillRect(32, 44 - 20, 20, 36);
        

        g2d.translate(42, 34);
        g2d.rotate(this.animationProgress);

        // DRAWING THE LEFT ARM
        if (player.game.renderer.lightingRenderer.torch.active) {
            System.out.println("Drawing torch");
            g2d.setColor(new Color(200, 0, 0));
            g2d.fillRect(-10, 25, 30, 10);
            g2d.setColor(new Color(200, 200, 0));
            g2d.fillRect(20, 25, 10, 10);
        }
        g2d.setColor(sleeveColor);
        g2d.fillRect(-5, 0, 10, 10);
        g2d.setColor(skinColor);
        g2d.fillRect(-5, 10, 10, 20);

        g2d.setTransform(originalTransform);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        handleAnimation();
        drawCharacter(g);
    }
}
