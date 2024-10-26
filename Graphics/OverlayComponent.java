package Graphics;

import java.awt.*;
import javax.swing.*;

public class OverlayComponent extends JComponent {
    public JPanel panel;

    // public OverlayComponent(JFrame frame) {
    public OverlayComponent(JPanel panel) {
        this.panel = panel;
    }

    public void paint(Graphics g) {
        g.setColor(Color.red);
        Container root = panel.getRootPane();
        g.setColor(new Color(100,100,100,100));
        rPaint(root, g);
    }

    private void rPaint(Container cont, Graphics g) {
        for(int i=0; i<cont.getComponentCount(); i++) {
            Component comp = cont.getComponent(i);
            if(!(comp instanceof JPanel)) {
                int x = comp.getX();
                int y = comp.getY();
                int w = comp.getWidth();
                int h = comp.getHeight();
                g.drawRect(x+4,y+4,w-8,h-8);
                g.drawString(comp.getClass().getName(),x+10,y+20);
            }
            if(comp instanceof Container) {
                rPaint((Container)comp,g);
            }
        }
    }
}
