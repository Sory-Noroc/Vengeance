package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;

import java.awt.*;

import static com.vengeance.game.entity.Entity.direction.*;

public class KingNPC extends Entity {
    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D graphics2D) {

    }

    public KingNPC(GamePanel gp) {
        super(gp);
        drawDirection = IDLE_LEFT;
        walkDirection = IDLE_LEFT;
        speed = 1;
    }

    @Override
    public Entity setImages(direction direction, String imagePath, int width, int height, int count, int startY) {
        return null;
    }
}
