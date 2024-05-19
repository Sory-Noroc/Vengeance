package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {

    protected GamePanel gamePanel;
    public int worldX = 100, worldY = 100;
    public enum direction { WALK_LEFT, WALK_RIGHT, WALK_UP, WALK_DOWN, IDLE_LEFT, IDLE_RIGHT, ATTACK_LEFT, ATTACK_RIGHT }

    public int speed;
    public BufferedImage[] walkLeft;
    public BufferedImage[] walkRight;
    public BufferedImage[] idleLeft;
    public BufferedImage[] idleRight;
    public BufferedImage[] attackLeft;
    public BufferedImage[] attackRight;
    public direction walkDirection;
    public direction drawDirection;
    private int spriteCounter = 0;
    private int spriteNumber = 1;
    public Rectangle collisionArea = new Rectangle(0,0,32, 32);
    private boolean collisionOn = false;
    protected int width, height;
    protected int drawWidth, drawHeight;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public int maxLife, life;

    public abstract void update();
    public abstract void draw(Graphics2D graphics2D);

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public String getStringFromDirection() {
        String dir = switch (walkDirection) {
            case WALK_LEFT -> "WALK_LEFT";
            case WALK_RIGHT -> "WALK_RIGHT";
            case WALK_UP -> "WALK_UP";
            case WALK_DOWN -> "WALK_DOWN";
            case IDLE_LEFT -> "IDLE_LEFT";
            case IDLE_RIGHT -> "IDLE_RIGHT";
            case ATTACK_LEFT -> "ATTACK_LEFT";
            case ATTACK_RIGHT -> "ATTACK_RIGHT";
        };
        return dir;
    }

    public direction getDirectionFromString(String direction) {
        direction dir = switch (direction) {
            case "WALK_LEFT" -> Entity.direction.WALK_LEFT;
            case "WALK_RIGHT" -> Entity.direction.WALK_RIGHT;
            case "WALK_UP" -> Entity.direction.WALK_UP;
            case "WALK_DOWN" -> Entity.direction.WALK_DOWN;
            case "IDLE_LEFT" -> Entity.direction.IDLE_LEFT;
            case "IDLE_RIGHT" -> Entity.direction.IDLE_RIGHT;
            case "ATTACK_LEFT" -> Entity.direction.ATTACK_LEFT;
            case "ATTACK_RIGHT" -> Entity.direction.ATTACK_RIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
        return dir;
    }

    public int getWorldX() {
        return worldX;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public BufferedImage[] getWalkRight() {
        return walkRight;
    }

    public BufferedImage[] getWalkLeft() {
        return walkLeft;
    }

    protected BufferedImage[] getAttackRight() { return attackRight; }

    protected BufferedImage[] getAttackLeft() { return attackLeft; }

    public abstract void setImages(direction direction, String imagePath, int width, int height, int count, int startY);

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

    public void setDirection(direction direction) {
        this.walkDirection = direction;
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public void setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
    }

    public int getSpriteNumber() {
        return spriteNumber;
    }

    public void setSpriteNumber(int spriteNumber) {
        this.spriteNumber = spriteNumber;
    }

    public Rectangle getCollisionArea() {
        return collisionArea;
    }

    public void setCollisionArea(Rectangle collisionArea) {
        this.collisionArea = collisionArea;
    }

    public boolean isCollisionOn() {
        return collisionOn;
    }

    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }
}
