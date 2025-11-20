package com.mycompany.student_enrollment_system; 
// NOTE: Make sure the package name above matches your project's package!

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.BasicStroke;

public class RoundedPanel extends JPanel {

    private int radius; 

    // --- CONSTRUCTORS ---

    // Default Constructor 
    public RoundedPanel() {
        this(20); 
    }

    // Custom Constructor (Used by new RoundedPanel(50) in NetBeans)
    public RoundedPanel(int radius) {
        this.radius = radius;
        // CRITICAL: Must be false so the custom shape can be drawn!
        setOpaque(false); 
    }

    // --- DRAWING LOGIC ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        Graphics2D g2 = (Graphics2D) g;
        
        // Enable Anti-Aliasing for smooth curves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();

        // --- 1. Draw the FILLED INTERIOR (Background Color) ---
        g2.setColor(getBackground()); 
        
        // Fill the rounded shape (using the crucial -1 fix)
        g2.fillRoundRect(0, 0, width - 1, height - 1, radius, radius); 

        // --- 2. Draw the ROUNDED OUTLINE (The Border) ---
        
        // Define the border properties 
        Color borderColor = Color.BLACK; // **THIS IS THE BLACK OUTLINE**
        int thickness = 3; // 3-pixel thickness

        // Set the color and thickness for the border line
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(thickness));
        
        // Draw the border outline
        g2.drawRoundRect(
            thickness / 2, // Start slightly inward to center the line
            thickness / 2, 
            width - thickness, // Reduce size to fit the border line
            height - thickness, 
            radius, 
            radius
        );
    }
}