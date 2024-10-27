package Graphics;

import Objects.Inventory;
import Terrain.FileHandler;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * The renderer for the inventory.
 */
public class InventoryRenderer extends JPanel {
    private final int framePixelSize = 72;
    private final int gapLength = 5;
    private BufferedImage frameImg = FileHandler.getBufferedImage("./Assets/InventoryFrame.png");
    private JLabel[] frames = new JLabel[Inventory.INV_SIZE];
    private Inventory inventory;

    /**
     * Creates a new inventory renderer.
     * @param inventory The inventory object.
     */
    public InventoryRenderer(Inventory inventory) {
        this.inventory = inventory;
        
        FlowLayout layout = new FlowLayout();
        layout.setHgap(gapLength);
        layout.setVgap(gapLength);
        this.setLayout(layout);

        // this.setOpaque(false);
        this.setBackground(new Color(20, 20, 20, 150));
        this.setBounds(Renderer.windowWidth / 2 - (framePixelSize + gapLength)
            * Inventory.INV_SIZE / 2, Renderer.windowHeight - 170 + gapLength, 
            (framePixelSize + gapLength) * Inventory.INV_SIZE, framePixelSize + gapLength * 2);
        this.setVisible(true);
        
        for (int i = 0; i < Inventory.INV_SIZE; i++) {
            Image img = frameImg.getScaledInstance(framePixelSize, 
                framePixelSize, Image.SCALE_SMOOTH);
            JLabel frame = new JLabel(new ImageIcon(img), JLabel.CENTER);
            frames[i] = frame;
            this.add(frame);
        }
    }

    /**
     * Renders the inventory.
     * @param g The graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        Point p = frames[inventory.currentlySelected].getLocation();
        g2d.setStroke(new BasicStroke(gapLength + 2));
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.drawRect(p.x, p.y, framePixelSize, framePixelSize);
    }

    /**
     * Paints the inventory items.
     * @param g The graphics object.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        Point p;

        for (int i = 0; i < inventory.items.length; i++) {
            byte tileType = inventory.items[i];
            if (tileType == 0) {
                continue;
            }
            BufferedImage sprite = TerrainRenderer.tileSprites.get(tileType);
            if (sprite != null) {
                p = frames[i].getLocation();
                g.drawImage(sprite, p.x + framePixelSize / 4, p.y + framePixelSize / 5, 
                    36, 36, null);
            }
        }
    }
}
