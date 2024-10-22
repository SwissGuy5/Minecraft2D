package Graphics;

import java.util.ArrayList;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import Terrain.*;

public class LightingRenderer extends JPanel {
    ArrayList<String> lights = new ArrayList<String>(); 


    public LightingRenderer() {
        this.setOpaque(false);
    }

    void drawLight(Graphics g) {
        // THIS IS WHERE THE MAGIC HAPPENS
        Graphics2D g2d = (Graphics2D) g;
        // ALLOWING ROTATIONS
        AffineTransform originalTransform = g2d.getTransform();

        // DRAWING THE RIGHT LEG
        g2d.translate(110, 120);
        g2d.rotate(1);
        g2d.setColor(new Color(0, 0, 255));
        g2d.fillRect(-10, 0, 20, 90);

        g2d.setTransform(originalTransform);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLight(g);
    }
}
