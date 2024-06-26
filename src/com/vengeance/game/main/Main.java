package com.vengeance.game.main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
//        mirrorImage("C:\\Users\\sorin\\OneDrive\\Desktop\\Uni\\An 2\\Sem 2\\PAOO\\VengeanceGame\\src\\resources\\images\\enemies\\FireWarrior.png", "C:\\Users\\sorin\\OneDrive\\Desktop\\Uni\\An 2\\Sem 2\\PAOO\\VengeanceGame\\src\\resources\\images\\enemies\\leftFireWarrior.png");
        imageCompletion();
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

    private static void imageCompletion() {
        String src = "C:\\Users\\sorin\\OneDrive\\Desktop\\Uni\\An 2\\Sem 2\\PAOO\\1207A_NorocSorin_et3\\src\\resources\\images\\player\\Viking-Sheet.png";
        String dest = "C:\\Users\\sorin\\OneDrive\\Desktop\\Uni\\An 2\\Sem 2\\PAOO\\1207A_NorocSorin_et3\\src\\resources\\images\\player\\LeftViking-Sheet.png";

        // BufferedImage for source image
        BufferedImage srcImg = null, destImg = null;

        // File object
        File f;

        // Read source image file
        try {
            f = new File(dest);
            destImg = ImageIO.read(f);
            f = new File(src);
            srcImg = ImageIO.read(f);
        }

        catch (IOException e) {
            System.out.println("Error: " + e);
        }

        // Get source image dimension
        int fullWidth = destImg.getWidth();
        int fullHeight = destImg.getHeight();

        // BufferedImage for mirror image
        BufferedImage temp = new BufferedImage(
                fullWidth, fullHeight, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < fullHeight; i++) {
            for (int j = 0; j < fullWidth; j++) {
                int p = destImg.getRGB(j, i);
                // set mirror image pixel value
                temp.setRGB(j, i, p);
            }
        }

        int width = 115, height = 84, rows = 4, cols = 1;
        int startY = 8 * height;

        for (int lj = rows - 1, rj = rows; lj >= 0; lj--, rj++) {
            int startLj = lj * width, startRj = rj * width;
            // Create mirror image pixel by pixel
            for (int y = startY; y < startY + height; y++) {
                for (int lx = startLj, rx = startRj; lx < startLj + width; lx++, rx++) {
                    // lx starts from the left side of the image
                    // rx starts from the right side of the
                    // image lx is used since we are getting
                    // pixel from left side rx is used to set
                    // from right side get source pixel value
                    int p = srcImg.getRGB(lx, y);

                    // set mirror image pixel value
                    temp.setRGB(rx, y, p);
                }
            }
        }

        // save mirror image
        try {
            f = new File(dest);
            ImageIO.write(temp, "png", f);
        }
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    private static void mirrorImage(String inputPath, String outputPath) {
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


        int width = 115, height = 84, rows = 16, cols = 25;

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
