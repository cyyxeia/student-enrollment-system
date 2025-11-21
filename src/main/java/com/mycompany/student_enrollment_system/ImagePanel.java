package com.mycompany.student_enrollment_system; // <--- MAKE SURE THIS MATCHES YOUR PACKAGE

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private Image bgImage;

    public ImagePanel(String path) {
        try {
            // Load image from resources
            bgImage = new ImageIcon(getClass().getResource(path)).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setOpaque(false); // Make panel transparent so children draw on top
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bgImage != null) {
            // Draw image covering the whole panel
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}