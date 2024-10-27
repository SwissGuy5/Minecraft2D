package Graphics;

import java.util.ArrayList;
import Objects.Game;
import Objects.Player;
import Objects.Light;
import Objects.Sun;
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
    Sun sun;
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

        // Creating the sun
        Sun sun = new Sun(200, 0, 10000, this.player);
        this.sun = sun;
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

    void drawSun(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(this.sun.x - 25, this.sun.y - 25, 50, 50);
    }

    void drawLight(Graphics g, Light light) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(light.x - 25, light.y - 25, 50, 50);
    }

    Rectangle[] getAllLightCollisionRectangles() {
        ArrayList<Rectangle> obstacles = new ArrayList<Rectangle>();
        for (int i = 0; i < 2; i++) {
            Rectangle[] temp = this.terrain.getLightCollisionRectangles(i);
            for (int j = 0; j < temp.length; j++) {
                obstacles.add(temp[j]);
            }
        }
        return obstacles.toArray(new Rectangle[obstacles.size()]);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        long now = System.currentTimeMillis();

        this.sun.update(16);
        this.drawSun(g2d);

        for (int y = 0; y < pixelArrayHeight; y++) {
            for (int x = 0; x < pixelArrayWidth; x++) {
                pixels[y][x] = 0;
            }
        }

        obstacles = this.getAllLightCollisionRectangles();

        // System.out.println("==================");
        // System.out.println(obstacles.length);
        for (int n = 0; n < obstacles.length; n++) {
            // System.out.println(obstacles[n].points[0][0] / 24);
        }

        for (int i = -1; i < this.lights.size(); i++) {
            if (i == -1) {
                light = this.sun;
            } else {
                light = this.lights.get(i);
            }

            if (!light.active) {
                continue;
            }

            polygons = new LightPolygon[obstacles.length];
            
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
                    int tX = x * pixelSize + this.player.x - 600;
                    int tY = y * pixelSize + this.player.y + 400;

                    int distanceSq = (lX - tX) * (lX - tX) + (lY - tY) * (lY - tY);
                    if (distanceSq < lightRadiusSq || light.strength == 0) {
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
                            if (light.strength == 0) {
                                pixels[y][x] += 25;
                            } else {
                                pixels[y][x] += 25 * (1 - distanceSq / lightRadiusSq);
                            }
                        } else {
                            if (light.strength == 0) {
                                pixels[y][x] -= 100;
                            } else {
                                pixels[y][x] -= light.strength * (1 - distanceSq / lightRadiusSq);
                            }
                        }
                    }
                }
            }
        }

        int alpha;

        for (int y = 0; y < pixelArrayHeight; y++) {
            for (int x = 0; x < pixelArrayWidth; x++) {
                alpha = pixels[y][x] + 100;
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