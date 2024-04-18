package com.vengeance.game.main;

import com.vengeance.game.object.Key;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class UI {
    GamePanel gamePanel;
    Font arial_40, arial_160;
    Key key;
    int messageCounter = 0;
    public boolean messageOn = true;
    public String message = "";
    public int commandNum = 0;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_160 = new Font("Arial", Font.PLAIN, 160);
        key = new Key(gamePanel);
    }

    public void setMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void showKeyCount(Graphics2D g) {
        g.setFont(arial_40);
        g.setColor(Color.white);
        g.drawImage(key.image, 0, 0, 2*gamePanel.getTileSize(), 2*gamePanel.getTileSize(), null);
        g.drawString("x " + gamePanel.player.keysGathered, 60, 40);

    }

    public void showMessageIfExists(Graphics2D g, int x, int y) {
        if (messageOn == true) {
            g.setFont(g.getFont().deriveFont(30f));
            g.drawString(message, x, y);
            messageCounter++;
            if (messageCounter >= message.length() * 10) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }

    public void draw(Graphics2D g) {
        if (gamePanel.gameState == GamePanel.GAME_STATE.PLAY_STATE) {
            showKeyCount(g);
            showMessageIfExists(g, gamePanel.getTileSize()/2, gamePanel.getTileSize()*5);
        } else if (gamePanel.gameState == GamePanel.GAME_STATE.PAUSE_STATE) {
            showPauseScreen(g);
        } else if (gamePanel.gameState == GamePanel.GAME_STATE.MENU_STATE) {
            drawTitleScreen(g);
        }
    }

    public void drawTitleScreen(Graphics2D g) {
        String imagePath = "/resources/images/backgrounds/menuBackground.jpg";
        BufferedImage background = null;
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(background, 0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight(), null);

        String title = "Vengeance";
        g.setFont(new Font("Times New Roman", Font.PLAIN, 80));
        int x = getXCentered(g, title);
        int y = gamePanel.getTileSize()*8;

        drawWithShadow(g, title, Color.white, Color.darkGray, x, y);

        // Player
        g.drawImage(gamePanel.player.getPlayerImage(), gamePanel.player.getScreenX(), gamePanel.player.getScreenY(), gamePanel.player.getDrawWidth(), gamePanel.player.getDrawHeight(), null);

        // Buttons
        String text = "New Game";
        x = getXCentered(g, text);
        y += gamePanel.getTileSize() * 9;
        drawWithShadow(g,text, Color.white, Color.darkGray, x, y);
        if (commandNum == 0) {
            g.drawString(">", x - gamePanel.getTileSize() * 2, y);
        }

        text = "Load Game";
        x = getXCentered(g, text);
        y += gamePanel.getTileSize() * 3;
        drawWithShadow(g,text, Color.white, Color.darkGray, x, y);
        if (commandNum == 1) {
            g.drawString(">", x - gamePanel.getTileSize() * 2, y);
        }

        text = "Quit";
        x = getXCentered(g, text);
        y += gamePanel.getTileSize() * 3;
        drawWithShadow(g,text, Color.white, Color.darkGray, x, y);
        if (commandNum == 2) {
            g.drawString(">", x - gamePanel.getTileSize() * 2, y);
        }
    }

    public void drawWithShadow(Graphics2D g, String text, Color mainColor, Color shadow, int x, int y) {
        g.setColor(shadow);
        g.drawString(text, x+3, y+3);
        g.setColor(mainColor);
        g.drawString(text, x, y);
    }

    public void showPauseScreen(Graphics2D g) {
        g.setFont(arial_160);
        g.setColor(Color.white);
        String text = "ll";
        g.drawString(text, gamePanel.getScreenWidth()/2 - 30, gamePanel.getScreenHeight()/2 + 20);
    }

    public int getXCentered(Graphics2D g, String text) {
        int length = (int)g.getFontMetrics().getStringBounds(text, g).getWidth();
        return gamePanel.getScreenWidth()/2 - length/2;
    }
}
