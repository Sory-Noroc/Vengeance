package com.vengeance.game.entity;

import com.vengeance.game.main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static java.lang.System.exit;

public abstract class Entity {

    private int worldX, worldY;
    private int speed;
    private BufferedImage[] left = new BufferedImage[6];
    private BufferedImage[] right = new BufferedImage[6];
    private String direction;
    private int spriteCounter = 0;
    private int spriteNumber = 1;
    public Rectangle collisionArea;
    private boolean collisionOn = false;
    protected int width, height;
    protected int drawWidth, drawHeight;
    public int solidAreaDefaultX, solidAreaDefaultY;

    public abstract void update();
    public abstract void draw(Graphics2D graphics2D);

    public int getWorldX() {
        return worldX;
    }

    public Entity setWorldX(int worldX) {
        this.worldX = worldX;
        return this;
    }

    public int getWorldY() {
        return worldY;
    }

    public Entity setWorldY(int worldY) {
        this.worldY = worldY;
        return this;
    }

    public int getSpeed() {
        return speed;
    }

    public Entity setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public Entity setImages(String direction, String imagePath, int width, int height, int rows, int cols) {
        BufferedImage sprite = null;
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (IOException e) {
            e.printStackTrace();
            exit(-1);
        }

        BufferedImage[] dir = left;
        if (direction.equals("right")) {
            dir = right;
        }
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dir[count] = sprite.getSubimage(width * j, height * i, width, height);
                dir[count] = UtilityTool.scaleImage(dir[count], drawWidth, drawHeight);
                count++;
            }
        }
        return this;
    }

    public BufferedImage[] getLeft() { return left; }

    public BufferedImage[] getRight() { return right; }

    public int getDrawWidth() {
        return drawWidth;
    }

    public int getDrawHeight() {
        return drawHeight;
    }

    public String getDirection() {
        return direction;
    }

    public Entity setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public Entity setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
        return this;
    }

    public int getSpriteNumber() {
        return spriteNumber;
    }

    public Entity setSpriteNumber(int spriteNumber) {
        this.spriteNumber = spriteNumber;
        return this;
    }

    public Rectangle getCollisionArea() {
        return collisionArea;
    }

    public Entity setCollisionArea(Rectangle collisionArea) {
        this.collisionArea = collisionArea;
        return this;
    }

    public boolean isCollisionOn() {
        return collisionOn;
    }

    public Entity setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
        return this;
    }
}
