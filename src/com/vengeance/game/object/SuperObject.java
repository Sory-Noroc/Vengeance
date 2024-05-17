package com.vengeance.game.object;

import com.vengeance.game.entity.Entity;
import com.vengeance.game.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject extends Entity {
    public BufferedImage image;
    public enum Object { CHEST, KEY, HEART }
    public Object name;
    public boolean collision = false;
    public int width = 1, height = 1; // Width and Height in tiles
    public int worldX, worldY;
    public Rectangle collisionArea;
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    @Override
    public void update() {

    }

    SuperObject(GamePanel panel) {
        super(panel);
        collisionArea = new Rectangle(0, 0, panel.getTileSize() * width, panel.getTileSize() * height);
    }

    @Override
    public void setImages(direction direction, String imagePath, int width, int height, int count, int startY) {
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.getScreenX();
        int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.getScreenY();

        if (worldX + gamePanel.player.getDrawWidth() > gamePanel.player.getWorldX() - gamePanel.player.getScreenX() &&
                worldY + gamePanel.player.getDrawHeight() > gamePanel.player.getWorldY() - gamePanel.player.getScreenY() &&
                worldX - gamePanel.player.getDrawWidth() < gamePanel.player.getWorldX() + gamePanel.player.getScreenX() &&
                worldY - gamePanel.player.getDrawHeight() < gamePanel.player.getWorldY() + gamePanel.player.getScreenY()) {
            graphics2D.drawImage(image, screenX, screenY, gamePanel.getTileSize() * width, gamePanel.getTileSize() * height, null);
        }
    }
}
