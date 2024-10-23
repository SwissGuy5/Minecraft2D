package Graphics;

import java.util.ArrayList;
import Objects.Light;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import Terrain.*;

public class LightingRenderer extends JPanel {
    Terrain terrain;
    ArrayList<Light> lights = new ArrayList<Light>(); 

    public LightingRenderer(Terrain terrain) {
        this.terrain = terrain;
        this.setOpaque(false);
    }

    void addLight(Light light) {
        this.lights.add(light);
    }

    void removeLight(int id) {
        int index = -1;
        for (int i = 0; i < this.lights.size(); i++) {
            if (this.lights.get(i).id == id) {
                index = i;
            }
        }
        if (index != -1) {
            this.lights.remove(index);
        }
    }

    void drawLight(Graphics g, Light light) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(light.x - 25, light.y - 25, 50, 50);

        int[]points = {light.x, light.y, 100, 0, 50, 100};
        drawTriangle(g2d, points, new GradientPaint(light.x, light.y, new Color(0, 0, 0, 0), 50, 100, new Color(0, 0, 0, 255)));

        g2d.setTransform(originalTransform);
    }

     void drawTriangle(Graphics2D g2d, int[]points, GradientPaint paint) {
        g2d.setPaint(paint);
        int[] pointsX = {points[0], points[2], points[4]};
        int[] pointsY = {points[1], points[3], points[5]};
        g2d.fillPolygon(pointsX, pointsY, 3);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < this.lights.size(); i++) {  
            drawLight(g, this.lights.get(i));
        }
    }
}
