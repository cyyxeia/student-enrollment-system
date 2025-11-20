/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.student_enrollment_system; 

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color; 
import java.awt.Dimension; 

public class ImagePanel extends JPanel {
    
    private Image backgroundImage;

    /**
     * Constructor loads the image from the resources folder.
     * @param imagePath The path to the image file (e.g., "/images/plmbg.png").
     */
    public ImagePanel(String imagePath) {
        // Load the image using the class loader to find the resource
        try {
             this.backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (Exception e) {
             System.err.println("Error loading background image: " + imagePath);
             // Set a fallback background color if the image fails to load
             setBackground(new Color(240, 240, 240)); 
             e.printStackTrace();
        }
       
        // CRITICAL: Must be non-opaque so child components draw on top of the image
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (backgroundImage != null) {
            // Draw the image, stretching it to fit the panel's current dimensions
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); 
        }
    }
}