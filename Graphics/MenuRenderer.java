package Graphics;

import Objects.Game;
import Terrain.FileHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

/**
 * The renderer for the menu screen.
 */
public class MenuRenderer extends JPanel {
    private boolean popupShowing = false;
    private boolean firstPopup = true;
    private Game game;
    private JButton selectSeedBtn = new JButton("New");

    /**
     * Creates a new menu renderer.
     * @param game The game object.
     */
    public MenuRenderer(Game game) {
        this.setBackground(new Color(50, 50, 50));
        this.setBounds(0, 0, Renderer.windowWidth, Renderer.windowHeight);
        this.setLayout(null);
        
        this.game = game;

        JLabel title = new JLabel("Minecraft 2D");
        title.setForeground(new Color(40, 200, 40));
        title.setBounds(Renderer.windowWidth / 2 - 300, Renderer.windowHeight / 2 - 200, 
            Renderer.windowWidth, 100);
        title.setFont(new Font("Century Schoolbook", Font.PLAIN, 100));
        this.add(title);

        JPopupMenu seedMenu = new JPopupMenu("Popup");
        String[] seeds = FileHandler.existingFileNames();
        seedMenu.add(newMenuItem("New.json"));
        for (int i = 0; i < seeds.length; i++) {
            seedMenu.add(newMenuItem(seeds[i]));
        }
        seedMenu.setPreferredSize(new Dimension(400, seeds.length * 60));
        this.add(seedMenu);
        
        selectSeedBtn.setBackground(new Color(120, 120, 120));
        selectSeedBtn.setBounds(Renderer.windowWidth / 2 - 200, 
            Renderer.windowHeight / 2 + 20, 400, 40);
        selectSeedBtn.setFocusPainted(false);
        selectSeedBtn.setFont(new Font("Century Schoolbook", Font.PLAIN, 20));
        selectSeedBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (popupShowing) {
                    popupShowing = false;
                    seedMenu.setVisible(false);
                } else {
                    popupShowing = true;
                    if (firstPopup) {
                        firstPopup = false;
                        seedMenu.show(seedMenu, selectSeedBtn.getX(), 
                            selectSeedBtn.getY());
                        seedMenu.setVisible(false);
                        seedMenu.setVisible(true);
                    } else {
                        seedMenu.setVisible(true);
                    }
                }         
            }
        });
        this.add(selectSeedBtn);
        
        JButton startBtn = new JButton("Start");
        startBtn.setBackground(new Color(120, 120, 120));
        startBtn.setBounds(
            Renderer.windowWidth / 2 - 100, Renderer.windowHeight / 2 + 100, 200, 70);
        startBtn.setFocusPainted(false);
        startBtn.setFont(new Font("Century Schoolbook", Font.PLAIN, 40));
        startBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(selectSeedBtn.getText());
                if (selectSeedBtn.getText().equals("New")) {
                    MenuRenderer.this.game.init();
                } else {
                    MenuRenderer.this.game.init(
                        FileHandler.fileNameToSeed(selectSeedBtn.getText()));
                }
            }
        });
        this.add(startBtn);
    }

    private JMenuItem newMenuItem(String name) {
        JMenuItem item = new JMenuItem(name.substring(0, name.length() - 5));
        item.setBackground(new Color(120, 120, 120));
        item.setPreferredSize(new Dimension(100, 20));
        selectSeedBtn.setFont(new Font("Century Schoolbook", Font.PLAIN, 60));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                popupShowing = false;
                selectSeedBtn.setText(item.getText());
            }
        });
        return item;
    }
}