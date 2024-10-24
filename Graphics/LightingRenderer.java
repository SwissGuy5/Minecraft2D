package Graphics;

import java.util.ArrayList;
import Objects.Light;
import Objects.Rectangle;
import Objects.LightPolygon;
import java.util.Collections;
import java.util.Comparator;

import java.awt.geom.Point2D;
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

        // int[]points = {light.x, light.y, 100, 0, 50, 100};
        // drawTriangle(g2d, points, new GradientPaint(light.x, light.y, new Color(255, 255, 255, 55), 50, 100, new Color(0, 0, 0, 255)));

        g2d.setTransform(originalTransform);
    }

    void drawTriangle(Graphics2D g2d, int[]points, GradientPaint paint) {
        g2d.setPaint(paint);
        int[] pointsX = {points[0], points[2], points[4]};
        int[] pointsY = {points[1], points[3], points[5]};
        g2d.fillPolygon(pointsX, pointsY, 3);
    }

    public boolean pointInPolygon(int x, int y, int[]xCoords, int[]yCoords) {
        return new Polygon(xCoords, yCoords, xCoords.length).contains(new Point(x, y));
    }

    public void paintComponent(Graphics g) {
        // UPDATE POSITION OF THE SUN
        if (this.lights.get(0).x > 50) {
            this.lights.get(0).x = this.lights.get(0).x - 1;
        }

        int pixelSize = 4;
        int pixelArrayWidth = 1200 / pixelSize;
        int pixelArrayHeight = 800 / pixelSize;

        Graphics2D g2d = (Graphics2D) g;
        byte[][] pixels = new byte[pixelArrayHeight][pixelArrayWidth];

        // ArrayList<Rectangle> obstacles = this.terrain.getLightCollisionRectangles(0);
        ArrayList<Rectangle> obstacles = new ArrayList<>();
        obstacles.add(new Rectangle(new int[]{12 * 10, 64 * 12 - 12 * 50, 12 * 12, 64 * 12 - 12 * 50, 12 * 10, 64 * 12 - 12 * 52, 12 * 12, 64 * 12 - 12 * 52}));

        for (int i = 0; i < this.lights.size(); i++) {
            ArrayList<LightPolygon> polygons = new ArrayList<>();

            ArrayList<Double> angles = new ArrayList<>();
            Light light = this.lights.get(i);

            Point2D center = new Point2D.Float(light.x, light.y);
            float radius = light.strength;
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {new Color(255, 255, 255, 55), new Color(0, 0, 0, 0)};
            RadialGradientPaint radialGradient = new RadialGradientPaint(center, radius, dist, colors);

            // // Approach of filling background with black doesn't work
            // g2d.setColor(new Color(0, 0, 0, 100));
            // g2d.fillRect(0, 0, 1200, 800);

            // g2d.setPaint(radialGradient);
            // g2d.fillOval(light.x - (int) radius, light.y - (int) radius, (int) radius * 2, (int) radius * 2);
            
            // fill polygon for all angles
            // g2d.fillPolygon(xs, ys, n);

            // [[x, y, a], [x, y, a], [x, y, a]]

            // // SLOPE CALCULATION
            // double deltaX = reachablePoints[k] - light.x;
            // double deltaY = reachablePoints[k + 1] - light.y;
            // double angle = Math.atan2(deltaY, deltaX);
            // angles.add(angle);
            
            for (int j = 0; j < obstacles.size(); j++) {
                Rectangle obstacle = obstacles.get(j);
                int[] reachablePoints = obstacle.getPointsReachableFrom(light.x, light.y);

                LightPolygon poly = new LightPolygon(reachablePoints);
                poly.calculateShadowForLightSource(light.x, light.y);
                polygons.add(poly);
                // g2d.drawPolygon(poly.xCoords, poly.yCoords, poly.n);

                // for (int k = 0; k < reachablePoints.length; k += 2) {
                //     double deltaX = reachablePoints[k] - light.x;
                //     double deltaY = reachablePoints[k + 1] - light.y;
                //     double angle = Math.atan2(deltaY, deltaX);
                //     angles.add(angle);
                // }
            }

            // // DEBUG DRAWING LINES
            // for (int j = 0; j < angles.size(); j++) {
            //     int lineLength = 1000;
            //     int endX = light.x + (int) (lineLength * Math.cos(angles.get(j)));
            //     int endY = light.y + (int) (lineLength * Math.sin(angles.get(j)));
            //     g.drawLine(light.x, light.y, endX, endY);
            // }
            
            // Collections.sort(angles, Comparator.reverseOrder());

            // for (int j = -1; j < angles.size(); j++) {
            //     Double startAngle;
            //     Double endAngle;
            //     if (j == -1) {
            //         startAngle = (double)0;
            //         endAngle = angles.get(0);
            //     } else if (j == angles.size() - 1) {
            //         startAngle = angles.get(angles.size() - 1);
            //         endAngle = (double)0;
            //     } else {
            //         startAngle = angles.get(j);
            //         endAngle = angles.get(j + 1);
            //     }
            
            //     // Define polygons, where light doesn't reach
            //     // go through pixel calculating the distance from center
            //     // if in one of the polygons, skip
            //     // depending on strength of light and distance assign a brightness
            // }

            for (int y = 0; y < pixelArrayHeight; y++) {
                for (int x = 0; x < pixelArrayWidth; x++) {
                    int transformedX = x * pixelSize;
                    int transformedY = y * pixelSize;

                    boolean isObscured = false;
                    for (int j = 0; j < polygons.size(); j++) {
                        LightPolygon poly = polygons.get(j);
                        Polygon polygon = new Polygon(poly.xCoords, poly.yCoords, poly.n);
                        if (polygon.contains(new Point(transformedX, transformedY))) {
                            // System.out.println("INSIDE");
                            isObscured = true;
                            break;
                        }
                    }
                    if (isObscured) {
                        pixels[y][x] += 50;
                    } else {
                        pixels[y][x] += 0;
                    }

                }
            }
        }

        for (int y = 0; y < pixelArrayHeight; y++) {
            for (int x = 0; x < pixelArrayWidth; x++) {
                g2d.setColor(new Color(0, 0, 0, (int)pixels[y][x]));
                g2d.fillRect(pixelSize * x, pixelSize * y, pixelSize, pixelSize);
            }
        }

        for (int i = 0; i < this.lights.size(); i++) {
            drawLight(g, this.lights.get(i));
        }
    }
}
