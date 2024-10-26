package Graphics;

import java.awt.*;
import javax.swing.JPanel;

import Terrain.FileHandler;

import java.awt.image.BufferedImage;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;


public class InventoryRenderer extends JPanel {
    private final int framePixelSize = 72;
    private final int gapLength = 5;
    private final int inventorySize = 9;
    private BufferedImage frameImg = FileHandler.getBufferedImage("./Assets/InventoryFrame.png");
    private JLabel[] frames = new JLabel[inventorySize];
    // private Image hotbarFrameImg = new Image()

    public InventoryRenderer() {
        FlowLayout layout = new FlowLayout();
        layout.setHgap(gapLength);
        layout.setVgap(0);
        this.setLayout(layout);

        this.setBounds(Renderer.windowWidth / 2 - (framePixelSize + gapLength) * inventorySize / 2, Renderer.windowHeight - 170, (framePixelSize + gapLength) * inventorySize, framePixelSize);
        // this.setBackground(new Color(0, 0, 0));
        this.setVisible(true);

        for (int i = 0; i < inventorySize; i++) {
            Image img = frameImg.getScaledInstance(framePixelSize, framePixelSize, Image.SCALE_SMOOTH);
            JLabel frame = new JLabel(new ImageIcon(img), JLabel.CENTER);
            // frame.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
            // frame.setSize(framePixelSize, framePixelSize);
            frames[i] = frame;
            this.add(frame);
        }
    }

    protected void paintComponent(Graphics g) {
        // super.paintComponent(g);
    }
}
