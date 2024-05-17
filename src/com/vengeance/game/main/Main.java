package com.vengeance.game.main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
//        mirrorImage("C:\\Users\\sorin\\OneDrive\\Desktop\\Uni\\An 2\\Sem 2\\PAOO\\VengeanceGame\\src\\resources\\images\\enemies\\Fire_Warrior.png", "C:\\Users\\sorin\\OneDrive\\Desktop\\Uni\\An 2\\Sem 2\\PAOO\\VengeanceGame\\src\\resources\\images\\enemies\\leftFire_Warrior.png");
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Vengeance");

        GamePanel gamePanel = GamePanel.getInstance();
        window.add(gamePanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }

    public static void mirrorImage(String inputPath, String outputPath) {
        // BufferedImage for source image
        BufferedImage fullImg = null;

        // File object
        File f;

        // Read source image file
        try {
            f = new File(inputPath);
            fullImg = ImageIO.read(f);
        }

        catch (IOException e) {
            System.out.println("Error: " + e);
        }

        // Get source image dimension
        int fullWidth = fullImg.getWidth();
        int fullHeight = fullImg.getHeight();

        int width = 144, height = 80, rows = 16, cols = 25;

        // BufferedImage for mirror image
        BufferedImage mimg = new BufferedImage(
                fullWidth, fullHeight, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < cols; i++) {
            int startY = i * height;
            for (int j = 0; j < rows; j++) {
                int startX = j * width;
                // Create mirror image pixel by pixel
                for (int y = startY; y < startY + height; y++) {
                    for (int lx = startX, rx = startX + width - 1; lx < startX + width; lx++, rx--) {

                        // lx starts from the left side of the image
                        // rx starts from the right side of the
                        // image lx is used since we are getting
                        // pixel from left side rx is used to set
                        // from right side get source pixel value
                        int p = fullImg.getRGB(lx, y);

                        // set mirror image pixel value
                        mimg.setRGB(rx, y, p);
                    }
                }
            }
        }

        // save mirror image
        try {
            f = new File(outputPath);
            ImageIO.write(mimg, "png", f);
        }
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
