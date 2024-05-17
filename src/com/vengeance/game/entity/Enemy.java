package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;
import static com.vengeance.game.entity.Entity.direction.*;
import static java.lang.Math.abs;

public class Enemy extends MobileEntity {

    public Enemy(GamePanel gamePanel) {
        super(gamePanel);
    }

    @Override
    public void setAction() {
        if (visible) {
            int playerX = gamePanel.player.worldX;
            int playerY = gamePanel.player.worldY;
            int distX = abs(worldX - playerX);
            int distY = abs(worldY - playerY);

            if (distX > distY) {
                // Move on x axis
                if (worldX < playerX) {
                    walkDirection = WALK_RIGHT;
                    drawDirection = WALK_RIGHT;
                } else if (worldX > playerX) {
                    walkDirection = WALK_LEFT;
                    drawDirection = WALK_LEFT;
                }
            } else {
                if (worldY < playerY) {
                    walkDirection = WALK_DOWN;
                } else {
                    walkDirection = WALK_UP;
                }
            }

        } else {
            // If not visible move randomly
            super.setAction();
        }
    }

    @Override
    public void update() {
        updateInvincible();

        if (!isAttacking) {
            setAction();
            collisionSpriteAndMovement();
            setCollisionOn(false);
            gamePanel.getCollisionChecker().checkPlayer(this);
            if (isCollisionOn()) {
                setAttacking(true);
            }
        } else {
            attackAnimation();
        }
    }
}
