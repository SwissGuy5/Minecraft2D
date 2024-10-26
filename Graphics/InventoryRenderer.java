package Graphics;

import java.awt.*;
import javax.swing.JPanel;

import Terrain.FileHandler;
import Objects.Inventory;

import java.awt.image.BufferedImage;
// import javax.swing.BorderFactory;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.ImageIcon;


public class InventoryRenderer extends JPanel {
    private final int framePixelSize = 72;
    private final int gapLength = 5;
    private BufferedImage frameImg = FileHandler.getBufferedImage("./Assets/InventoryFrame.png");
    private JLabel[] frames = new JLabel[Inventory.inventorySize];
    private int currentlySelected = 0;
    private int[] items = new int[Inventory.inventorySize];
    // private Image hotbarFrameImg = new Image()

    public InventoryRenderer(Renderer renderer) {
        FlowLayout layout = new FlowLayout();
        layout.setHgap(gapLength);
        layout.setVgap(gapLength);
        this.setLayout(layout);

        // this.setOpaque(false);
        this.setBackground(new Color(20, 20, 20, 150));
        this.setBounds(Renderer.windowWidth / 2 - (framePixelSize + gapLength) * Inventory.inventorySize / 2, Renderer.windowHeight - 170 + gapLength, (framePixelSize + gapLength) * Inventory.inventorySize, framePixelSize + gapLength * 2);
        this.setVisible(true);
        
        for (int i = 0; i < Inventory.inventorySize; i++) {
            Image img = frameImg.getScaledInstance(framePixelSize, framePixelSize, Image.SCALE_SMOOTH);
            JLabel frame = new JLabel(new ImageIcon(img), JLabel.CENTER);
            frames[i] = frame;
            this.add(frame);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;
        Point p = frames[currentlySelected].getLocation();
        g2d.setStroke(new BasicStroke(gapLength + 2));
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.drawRect(p.x, p.y, framePixelSize, framePixelSize);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        Point p;

        for (int i = 0; i < items.length; i++) {
            int tileType = items[i];
            if (tileType == 0) continue;
            BufferedImage sprite = TerrainRenderer.tileSprites.get(tileType);
            if (sprite != null) {
                p = frames[i].getLocation();
                g.drawImage(sprite, p.x + framePixelSize / 4, p.y + framePixelSize / 5, 36, 36, null);
            }
        }
    }

    public void update(Inventory inventory) {
        currentlySelected = inventory.currentlySelected;
        items = inventory.items;
    }
}
