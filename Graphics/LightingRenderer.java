package Graphics;

import java.util.ArrayList;
import Objects.Game;
import Objects.Player;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.File;
import Terrain.*;

public class LightingRenderer extends JPanel {
    Terrain terrain;
    Player player;
    ArrayList<Light> lights = new ArrayList<Light>();
    Rectangle[] obstacles;

    int pixelSize = 4;
    int pixelArrayWidth = 1200 / pixelSize;
    int pixelArrayHeight = 800 / pixelSize;
    int[][] pixels = new int[pixelArrayHeight][pixelArrayWidth];

    private Light light;
    private LightPolygon[] polygons;
    private Polygon[] awtPolygons;

    public LightingRenderer(Game game) {
        this.terrain = game.terrain;
        this.player = game.player;
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
        Graphics2D g2d = (Graphics2D) g;
        long now = System.currentTimeMillis();

        // // UPDATE POSITION OF THE SUN
        if (this.lights.get(0).x > 50) {
            this.lights.get(0).x = this.lights.get(0).x - 1;
        }

        for (int y = 0; y < pixelArrayHeight; y++) {
            for (int x = 0; x < pixelArrayWidth; x++) {
                pixels[y][x] = 100;
            }
        }

        obstacles = this.terrain.getLightCollisionRectangles(0);
        // ArrayList<Rectangle> obstacles = new ArrayList<>();
        // obstacles.add(new Rectangle(new int[]{12 * 10, 64 * 12 - 12 * 50, 12 * 12, 64 * 12 - 12 * 50, 12 * 10, 64 * 12 - 12 * 52, 12 * 12, 64 * 12 - 12 * 52}));

        for (int i = 0; i < this.lights.size(); i++) {
            polygons = new LightPolygon[obstacles.length];
            light = this.lights.get(i);
            
            for (int j = 0; j < obstacles.length; j++) {
                Rectangle obstacle = obstacles[j];
                if (obstacle.closestPointInReach(light.x, light.y, light.strength)) {
                    int[] reachablePoints = obstacle.getPointsReachableFrom(light.x, light.y);
                    LightPolygon poly = new LightPolygon(reachablePoints);
                    poly.calculateShadowForLightSource(light.x, light.y);
                    polygons[j] = poly;
                    // g2d.drawPolygon(poly.xCoords, poly.yCoords, poly.n);
                } else {
                    polygons[j] = null;
                }
            }

            awtPolygons = new Polygon[polygons.length];
            for (int j = 0; j < awtPolygons.length; j++) {
                LightPolygon poly = polygons[j];
                if (poly == null) {
                    continue;
                }
                awtPolygons[j] = new Polygon(poly.xCoords, poly.yCoords, poly.n);
            }

            double lightRadiusSq = light.strength * light.strength;
            int lX = light.x;
            int lY = light.y;

            for (int y = 0; y < pixelArrayHeight; y++) {
                for (int x = 0; x < pixelArrayWidth; x++) {
                    int tX = x * pixelSize;
                    int tY = y * pixelSize;

                    int distanceSq = (lX - tX) * (lX - tX) + (lY - tY) * (lY - tY);
                    if (distanceSq < lightRadiusSq) {
                        boolean isObscured = false;
                        for (int j = 0; j < awtPolygons.length; j++) {
                            if (awtPolygons[j] == null) {
                                continue;
                            }
                            if (awtPolygons[j].contains(new Point(tX, tY))) {
                                isObscured = true;
                                break;
                            }
                        }
                        if (isObscured) {
                            pixels[y][x] += 25 * (1 - distanceSq / lightRadiusSq);
                        } else {
                            pixels[y][x] -= light.strength * (1 - distanceSq / lightRadiusSq);
                        }
                    }
                }
            }
        }

        int alpha;

        for (int y = 0; y < pixelArrayHeight; y++) {
            for (int x = 0; x < pixelArrayWidth; x++) {
                alpha = pixels[y][x];
                if (alpha > 255) {
                    alpha = 255;
                }
                if (alpha < 0) {
                    alpha = 0;
                }
                g2d.setColor(new Color(0, 0, 0, alpha));
                g2d.fillRect(pixelSize * x, pixelSize * y, pixelSize, pixelSize);
            }
        }

        for (int i = 0; i < this.lights.size(); i++) {
            drawLight(g, this.lights.get(i));
        }

        long nower = System.currentTimeMillis();
        if (nower - now > 16) {
            // System.out.println(nower - now);
        }
    }
}