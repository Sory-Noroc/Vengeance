package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;

import java.awt.*;

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
                followOnX();
                handleAICollision('y');
            } else {
                followOnY();
                handleAICollision('x');
            }

        } else {
            // If not visible move randomly
            super.setAction();
        }
    }

    private void followOnY() {
        if (worldY + speed < gamePanel.player.worldY) {
            walkDirection = WALK_DOWN;
        } else if (worldY - speed > gamePanel.player.worldY) {
            walkDirection = WALK_UP;
        }
    }

    private void followOnX() {
        if (worldX + speed < gamePanel.player.worldX) {
            // Enemy is on the left of player
            walkDirection = WALK_RIGHT;
            drawDirection = WALK_RIGHT;

        } else if (worldX - speed > gamePanel.player.worldX) {
            // Enemy is on the right of player
            walkDirection = WALK_LEFT;
            drawDirection = WALK_LEFT;
        }
    }

    private void handleAICollision(Character direction) {
        setCollisionOn(false);
        gamePanel.getCollisionChecker().checkTile(this);
        if (isCollisionOn()) {
            switch (direction) {
                case 'y' -> followOnY();
                case 'x' -> followOnX();
            }
        }
    }

    @Override
    protected void setHealthBarPos(int screenX, int screenY) {
        super.setHealthBarPos(screenX, screenY);
        hpX += 40;
        hpY -= 60;
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

    protected void drawHealthBar(Graphics2D g) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        double oneScale = (double)healthBarWidth/maxLife;
        double hpValue = oneScale * life;
        g.setColor(new Color(35, 35, 35));
        g.fillRect(hpX, hpY, healthBarWidth, healthBarHeight);
        g.setColor(new Color(255, 0, 30));
        g.fillRect(hpX, hpY, (int)hpValue, healthBarHeight);
    }


    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        drawHealthBar(g);
    }
}
