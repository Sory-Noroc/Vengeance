package com.vengeance.game.main;

import com.vengeance.game.object.Heart;
import com.vengeance.game.object.Key;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class UI {
    GamePanel gamePanel;
    Font norse_40, norse_80, norse_160, arial_160;
    Key key;
    int messageCounter = 0;
    int drawSize;
    public boolean messageOn = true;
    BufferedImage heart_full, heart_half, heart_empty;
    public String message = "";
    public int commandNum = 0;
    public String currentDialog;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        try {
            arial_160 = new Font("Arial", Font.PLAIN, 160);
            norse_40 = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/resources/font/Norse.ttf"))).deriveFont(Font.PLAIN, 40);
            norse_160 = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/resources/font/Norse.ttf"))).deriveFont(Font.PLAIN, 160);
            norse_80 = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/resources/font/Norse.ttf"))).deriveFont(Font.PLAIN, 80);
        } catch (FontFormatException | IOException ex) {
            ex.printStackTrace();
        }
        key = new Key(gamePanel);

        Heart heart = new Heart(gamePanel);
        drawSize = heart.drawSize;
        heart_full = heart.heartFull;
        heart_half = heart.heartHalf;
        heart_empty = heart.heartEmpty;
    }

    public void setMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void showKeyCount(Graphics2D g) {
        g.setFont(norse_40);
        g.setColor(Color.white);
        g.drawString("x " + gamePanel.player.keysGathered, 60, 150);
        g.drawImage(key.image, 0, 100, 2*gamePanel.getTileSize(), 2*gamePanel.getTileSize(), null);
    }

    public void showMessageIfExists(Graphics2D g, int x, int y) {
        if (messageOn == true) {
            g.setFont(norse_80.deriveFont(Font.PLAIN, 30));
            g.drawString(message, x, y);
            messageCounter++;
            if (messageCounter >= message.length() * 10) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }

    private void showPlayerLife(Graphics2D g) {
        int x = gamePanel.getTileSize() / 2;
        int y = gamePanel.getTileSize() / 2;
        int i = 0;

        while(i < gamePanel.player.maxLife / 2) {
            g.drawImage(heart_empty, x, y, null);
            i++;
            x += drawSize;
        }

        x = gamePanel.getTileSize() / 2;
        y = gamePanel.getTileSize() / 2;
        i = 0;

        while (i < gamePanel.player.life) {
            g.drawImage(heart_half, x, y, null);
            i++;
            if (i < gamePanel.player.life) {
                g.drawImage(heart_full, x, y, null);
            }
            i++;
            x += drawSize;
        }
    }

    public void draw(Graphics2D g) {
        if (gamePanel.gameState == GamePanel.GAME_STATE.PLAY_STATE) {
            showPlayerLife(g);
            showKeyCount(g);
            showMessageIfExists(g, gamePanel.getTileSize()/2, gamePanel.getTileSize()*7);
        } else if (gamePanel.gameState == GamePanel.GAME_STATE.PAUSE_STATE) {
            showPlayerLife(g);
            showPauseScreen(g);
        } else if (gamePanel.gameState == GamePanel.GAME_STATE.MENU_STATE) {
            showPlayerLife(g);
            drawTitleScreen(g);
        } else if (gamePanel.gameState == GamePanel.GAME_STATE.DIALOG_STATE) {
            showPlayerLife(g);
            drawDialogueScreen(g);
        } else if (gamePanel.gameState == GamePanel.GAME_STATE.GAME_OVER_STATE) {
            drawGameOverScreen(g);
        }
    }

    public void drawGameOverScreen(Graphics2D g) {
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());

        int x;
        int y;
        String text;
        g.setFont(g.getFont().deriveFont(Font.BOLD, 110f));

        text = "You died!";
        g.setColor(Color.black);
        x = getXCentered(g, text);
        y = gamePanel.getTileSize() * 10;
        g.drawString(text, x, y);

        g.setColor(Color.white);
        g.drawString(text, x-4, y-4);

        g.setFont(g.getFont().deriveFont(50f));
        text = "Retry";
        x = getXCentered(g, text);
        y += gamePanel.getTileSize() * 5;
        g.drawString(text, x, y);
        if (commandNum == 0) {
            g.drawString(">", x-40, y);
        }

        text = "Quit";
        x = getXCentered(g, text);
        y += gamePanel.getTileSize() * 3;
        g.drawString(text, x, y);
        if (commandNum == 1) {
            g.drawString(">", x-40, y);
        }
    }

    public void drawDialogueScreen(Graphics2D g) {
        int x = gamePanel.getTileSize()*2;
        int y = gamePanel.getTileSize()/2;
        int width = gamePanel.getScreenWidth() - (gamePanel.getTileSize() * 4);
        int height = gamePanel.getTileSize() * 8;

        drawSubWindow(g, x, y, width, height);
        g.setFont(norse_40.deriveFont(Font.PLAIN, 30));
        x += gamePanel.getTileSize();
        y += 2 * gamePanel.getTileSize();
        for (String line: currentDialog.split("\n")) {
            g.drawString(line, x, y);
            y += g.getFont().getSize() * 2;
        }
    }

    public void drawSubWindow(Graphics2D g, int x, int y, int width, int height) {
        Color c = new Color(0,0,0, 230);
        g.setColor(c);
        g.fillRect(x, y, width, height);
        c = new Color(255,255,0);
        g.setColor(c);
        g.setStroke(new BasicStroke(5));
        g.drawRect(x+5, y+5, width-10, height-10);
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
        g.setFont(norse_80);
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
