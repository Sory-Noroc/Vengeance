package com.vengeance.game.tile;

import com.vengeance.game.Animation;

import java.awt.image.BufferedImage;

public class Tile {

    private BufferedImage image;
    private boolean collision = false;
    private Animation animation = null;

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public BufferedImage getImage() {
        if (animation != null) {
            image = animation.getImage();
        }
        return image;
    }

    public Tile setImage(BufferedImage image) {
        this.image = image;
        return this;
    }

    public boolean isCollision() {
        return collision;
    }

    public Tile setCollision(boolean collision) {
        this.collision = collision;
        return this;
    }
}
