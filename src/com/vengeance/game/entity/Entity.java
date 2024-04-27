package com.vengeance.game.entity;


import com.vengeance.game.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {

    protected GamePanel gamePanel;
    private int worldX, worldY;
    public enum direction {WALK_LEFT, WALK_RIGHT, WALK_UP, WALK_DOWN, IDLE_LEFT, IDLE_RIGHT};
    public int speed;
    public BufferedImage[] walkLeft;
    public BufferedImage[] walkRight;
    public BufferedImage[] idleLeft;
    public BufferedImage[] idleRight;
    public direction walkDirection;
    public direction drawDirection;
    private int spriteCounter = 0;
    private int spriteNumber = 1;
    public Rectangle collisionArea = new Rectangle(0,0,32, 32);
    private boolean collisionOn = false;
    protected int width, height;
    protected int drawWidth, drawHeight;
    public int solidAreaDefaultX, solidAreaDefaultY;

    public abstract void update();
    public abstract void draw(Graphics2D graphics2D);

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

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

    public BufferedImage[] getWalkRight() {
        return walkRight;
    }

    public BufferedImage[] getWalkLeft() {
        return walkLeft;
    }

    public abstract Entity setImages(direction direction, String imagePath, int width, int height, int count, int startY);

    public int getDrawWidth() {
        return drawWidth;
    }

    public int getDrawHeight() {
        return drawHeight;
    }

    public direction getDrawDirection() {
        return drawDirection;
    }

    public void setDrawDirection(direction dir) {
        drawDirection = dir;
    }

    public direction getDirection() {
        return walkDirection;
    }

    public Entity setDirection(direction direction) {
        this.walkDirection = direction;
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
