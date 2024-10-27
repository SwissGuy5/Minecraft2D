package Graphics;

import java.util.ArrayList;
import Objects.Game;
import Objects.Player;
import Objects.Light;
import Objects.Sun;
import Objects.Torch;
import Objects.Rectangle;
import Objects.LightPolygon;

import javax.swing.JPanel;
import java.awt.*;
import Terrain.*;

/**
 * The renderer for the lighting effects.
 */
public class LightingRenderer extends JPanel {
    Game game;
    Terrain terrain;
    Player player;
    Sun sun;
    Torch torch;
    ArrayList<Light> lights = new ArrayList<Light>();
    Rectangle[] obstacles;

    int pixelSize = 4;
    int pixelArrayWidth = 1200 / pixelSize;
    int pixelArrayHeight = 800 / pixelSize;
    int[][] pixels = new int[pixelArrayHeight][pixelArrayWidth];

    private Light light;
    private LightPolygon[] polygons;
    private Polygon[] awtPolygons;

    /**
     * Creates a new lighting renderer.
     * @param game The game object.
     */
    public LightingRenderer(Game game) {
        this.game = game;
        this.terrain = game.terrain;
        this.player = game.player;
        this.setBounds(0, 0, Renderer.windowWidth, Renderer.windowHeight);
        this.setOpaque(false);

        Sun sun = new Sun(this.player);
        this.sun = sun;
        Torch torch = new Torch( 500, this.player);
        this.torch = torch;
    }

    /**
     * Adds a light to the renderer.
     * @param light The light to add.
     */
    public void addLight(Light light) {
        this.lights.add(light);
    }

    /**
     * Removes a light from the renderer.
     * @param id The id of the light to remove.
     */
    public void removeLight(int id) {
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

    /**
     * Draws the sun in the light renderer.
     * @param g The graphics object.
     */
    void drawSun(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        int[] sunCoords = sun.getAnimationCoordinates();
        g2d.fillRect(sunCoords[0] - 25, sunCoords[1] - 25, 50, 50);
    }

    /**
     * Gets all the collision rectangles that the light can collide with.
     * @return An array of collision rectangles.
     */
    Rectangle[] getAllLightCollisionRectangles() {
        ArrayList<Rectangle> obstacles = new ArrayList<Rectangle>();
        int playerChunkOffset = player.chunkOffset();
        for (int i = -1; i < 2; i++) {
            Rectangle[] temp = this.terrain.getLightCollisionRectangles(playerChunkOffset + i);
            if (temp == null) {
                continue;
            }
            for (int j = 0; j < temp.length; j++) {
                obstacles.add(temp[j]);
            }
        }
        return obstacles.toArray(new Rectangle[obstacles.size()]);
    }

    /**
     * Paints the lighting effects. Applies shadows to the pixels.
     * @param g The graphics object.
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        this.sun.update(game.delta);
        this.torch.update(game.delta);
        this.drawSun(g2d);

        for (int y = 0; y < pixelArrayHeight; y++) {
            for (int x = 0; x < pixelArrayWidth; x++) {
                pixels[y][x] = 100;
            }
        }

        obstacles = this.getAllLightCollisionRectangles();

        for (int i = -2; i < this.lights.size(); i++) {
            if (i == -2) {
                light = this.sun;
            } else if (i == -1) {
                light = this.torch;
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
                    int tX = x * pixelSize + (int) (this.player.x * 48) - 600 + 23;
                    int tY = y * pixelSize - (int) (this.player.y * 48) + 2676;

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
                                pixels[y][x] -= 255;
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
    }
}