package com.vengeance.game.animation;

import java.awt.image.BufferedImage;

public class Animation {
    private int spriteCounter = 0;
    private int sprites;
    private int spriteNumber = 0;
    private int delay;
    private BufferedImage[] images;
    private BufferedImage currentImage;

    public int getSpriteNumber() {
        return spriteNumber;
    }

    public void setSpriteNumber(int spriteNumber) { this.spriteNumber = spriteNumber; }

    public Animation(int spriteCount, int delay, BufferedImage... images) {
        this.sprites = spriteCount;
        this.images = images;
        this.delay = delay;
        currentImage = images[0];
    }

    public void update() {
        spriteCounter++;
        if (spriteCounter > delay) {
            setSpriteNumber(getSpriteNumber() + 1);
            if (getSpriteNumber() == sprites) {
                setSpriteNumber(0);
            }
            currentImage = images[getSpriteNumber()];
            spriteCounter = 0;
        }
    }

    public BufferedImage getImage() { return currentImage; }
}
