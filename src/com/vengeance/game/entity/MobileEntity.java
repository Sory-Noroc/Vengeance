package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;

import java.awt.*;

public class MobileEntity extends Entity {
    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D graphics2D) {

    }

    MobileEntity(GamePanel gamePanel) {
        super(gamePanel);
    }

    @Override
    public Entity setImages(direction direction, String imagePath, int width, int height, int count, int startY) {
        return null;
    }
}
