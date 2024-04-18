package com.vengeance.game.object;

import com.vengeance.game.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image;
    public enum Object { CHEST, KEY }
    public Object name;
    public boolean collision = false;
    public int width = 1, height = 1; // Width and Height in tiles
    public int worldX, worldY;
    public Rectangle solidArea;
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    SuperObject(GamePanel panel) {
        solidArea = new Rectangle(0, 0, panel.getTileSize() * width, panel.getTileSize() * height);
    }

    public void draw(Graphics2D graphics2D, GamePanel gp) {
        int screenX = worldX - gp.player.getWorldX() + gp.player.getScreenX();
        int screenY = worldY - gp.player.getWorldY() + gp.player.getScreenY();
        graphics2D.drawImage(image, screenX, screenY, gp.getTileSize() * width, gp.getTileSize() * height, null);
    }
}
