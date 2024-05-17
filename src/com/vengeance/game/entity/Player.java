package com.vengeance.game.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import com.vengeance.game.main.GamePanel;
import com.vengeance.game.main.KeyHandler;
import com.vengeance.game.main.UtilityTool;
import com.vengeance.game.object.SuperObject;

import javax.imageio.ImageIO;

import static com.vengeance.game.entity.Entity.direction.*;
import static java.lang.System.exit;

public class Player extends MobileEntity {

    private final KeyHandler keyHandler;

    private final int screenX;
    private final int screenY;

    public int keysGathered = 0;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        super(gamePanel);
        this.keyHandler = keyHandler;
        spriteScale = gamePanel.getScale() - 2;
        totalSprites = 8;

        width = 115;
        height = 84;
        drawWidth = width * spriteScale;
        drawHeight = height * spriteScale;
        this.screenX = gamePanel.getScreenWidth() / 2 - (drawWidth / 2);
        this.screenY = gamePanel.getScreenHeight() / 2 - (drawHeight / 2);

        setAttackArea(24, 10);
        setCollisionArea(
                new Rectangle(
                42 * spriteScale,
                45 * spriteScale,
                32 * spriteScale,
                26 * spriteScale)
        );

        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;

        maxLife = 6;
        life = maxLife;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        setWorldX(gamePanel.getTileSize() * 25);
        setWorldY(gamePanel.getTileSize() * 21);
        setSpeed(4);
        life = maxLife;
        invincible = false;
        invincibleCount = 0;
        keysGathered = 0;
        setDirection(WALK_RIGHT);
        setDrawDirection(WALK_RIGHT);
    }

    public BufferedImage getPlayerImage() {
        setImages(direction.WALK_LEFT, "/resources/images/player/LeftViking-Sheet.png", width, height, 8, height);
        setImages(direction.WALK_RIGHT, "/resources/images/player/Viking-Sheet.png", width, height, 8, height);
        setImages(direction.IDLE_LEFT, "/resources/images/player/LeftViking-Sheet.png", width, height, 8, 0);
        setImages(direction.IDLE_RIGHT, "/resources/images/player/Viking-Sheet.png", width, height, 8, 0);
        setImages(direction.ATTACK_LEFT, "/resources/images/player/LeftViking-Sheet.png", width, height, 4, 8*height);
        setImages(direction.ATTACK_RIGHT, "/resources/images/player/Viking-Sheet.png", width, height, 4, 8*height);
        return getWalkRight()[1];
    }

    @Override
    protected void hit() {
        super.hit();
        gamePanel.playSE(2);
    }

    private boolean checkKeysPressed() {
        if (keyHandler.isUpPressed() ||
                keyHandler.isDownPressed() ||
                keyHandler.isLeftPressed() ||
                keyHandler.isRightPressed() ||
                keyHandler.spacePressed) {
            if (keyHandler.isUpPressed()) {
                setDirection(WALK_UP);
            } else if (keyHandler.isDownPressed()) {
                setDirection(WALK_DOWN);
            } else if (keyHandler.isLeftPressed()) {
                setDirection(WALK_LEFT);
                setDrawDirection(WALK_LEFT);
            } else if (keyHandler.isRightPressed()) {
                setDirection(WALK_RIGHT);
                setDrawDirection(WALK_RIGHT);
            }
            return true;
        }
        return false;
    }

    @Override
    public void update() {

        updateInvincible();

        // Attacking
        if (isAttacking) {
            int enemyIndex = gamePanel.getCollisionChecker().checkEntity(this, gamePanel.enemy, true);
            damageEnemy(enemyIndex);
            attackAnimation();
        } else if (checkKeysPressed()) {
            collisionSpriteAndMovement();
        }

        int objIndex = gamePanel.getCollisionChecker().checkObject(this, true);
        interactWithObject(objIndex);

        int npcIndex = gamePanel.getCollisionChecker().checkEntity(this, gamePanel.npc);
        interactWithNPC(npcIndex);

        int enemyIndex = gamePanel.getCollisionChecker().checkEntity(this, gamePanel.enemy);
        getHitByEnemy(enemyIndex);

        gamePanel.eventHandler.checkEvent();

        if (life <= 0) {
            gamePanel.gameState = GamePanel.GAME_STATE.GAME_OVER_STATE;
        }
    }

    private void damageEnemy(int i) {
        if (i != -1) {

            if (!gamePanel.enemy[i].invincible) {
                gamePanel.enemy[i].hit();

                if (gamePanel.enemy[i].life <= 0) {
                    gamePanel.enemy[i] = null;
                }
            }
        }
    }

    private void getHitByEnemy(int i) {
        if (i != -1) {
            if (!invincible) {
                hit();
            }
        }
    }

    private void interactWithObject(int i) {
        if (i != -1) {
            SuperObject.Object name = gamePanel.obj[i].name;
            switch (name) {
                case KEY -> {
                    keysGathered++;
                    gamePanel.playSE(1);
                    gamePanel.obj[i] = null;
                    gamePanel.ui.setMessage("You got a key!");
                }
                case CHEST -> {
                    // some action when we touch a chest
                    gamePanel.ui.setMessage("You opened a chest!");
                }
            }
        }
    }

    private void interactWithNPC(int i) {
        if (gamePanel.keyHandler.enterPressed) {
            if (i != -1) {
                gamePanel.gameState = GamePanel.GAME_STATE.DIALOG_STATE;
                gamePanel.npc[i].speak();
            }
        }
        if (gamePanel.keyHandler.spacePressed) {
            setAttacking(true);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (invincible) {
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        graphics2D.drawImage(getDirectionalImage(), screenX, screenY, null);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        drawDebugRects(graphics2D, screenX, screenY);

    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }
}
