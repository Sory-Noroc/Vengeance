package com.vengeance.game.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.vengeance.game.main.GamePanel;
import com.vengeance.game.events.KeyHandler;
import com.vengeance.game.object.SuperObject;

import static com.vengeance.game.entity.Entity.direction.*;

public class Player extends MobileEntity {

    private final KeyHandler keyHandler;

    private final int screenX;
    private final int screenY;
    public Rectangle attackArea = new Rectangle();
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
        if (gamePanel.currentMap == 0) {
            setWorldX(gamePanel.getTileSize() * 25);
            setWorldY(gamePanel.getTileSize() * 21);
            keysGathered = 200;

        } else if (gamePanel.currentMap == 1) {
            gamePanel.keyHandler.ePressed = true;
            gamePanel.eventHandler.teleport(1, 33, 38);

        } else if (gamePanel.currentMap == 2) {
            gamePanel.keyHandler.ePressed = true;
            gamePanel.eventHandler.teleport(2, 5, 39);
        }
        setSpeed(4);
        life = maxLife;
        invincible = true;
        invincibleCount = 0;
        setDirection(WALK_RIGHT);
        setDrawDirection(WALK_RIGHT);
    }

    protected void setAttackArea(int x, int y) {
        attackArea.x = x * spriteScale;
        attackArea.y = y * spriteScale;
        attackArea.width = (width - x * 2) * spriteScale;
        attackArea.height = (height - y * 2) * spriteScale;
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

    private boolean checkKeysPressed() {
        if (keyHandler.isUpPressed() ||
                keyHandler.isDownPressed() ||
                keyHandler.isLeftPressed() ||
                keyHandler.isRightPressed()) {
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

            if (!gamePanel.enemy[gamePanel.currentMap][i].invincible) {
                gamePanel.enemy[gamePanel.currentMap][i].hit();

                if (gamePanel.enemy[gamePanel.currentMap][i].life <= 0) {
                    gamePanel.enemy[gamePanel.currentMap][i].update();
                    gamePanel.enemy[gamePanel.currentMap][i] = null;
                }
            }
        }
    }

    private void getHitByEnemy(int i) {
        if (i != -1) {
            if (!invincible) {
                hit(i);
            }
        }
    }

    private void hit(int i) {
        if (!invincible) {
            life -= gamePanel.enemy[gamePanel.currentMap][i].damage;
            invincible = true;
        }
        gamePanel.playSE(2);
    }

    private void interactWithObject(int i) {
        if (i != -1) {
            SuperObject.Object name = gamePanel.obj[gamePanel.currentMap][i].name;
            switch (name) {
                case KEY -> {
                    keysGathered++;
                    gamePanel.playSE(1);
                    gamePanel.obj[gamePanel.currentMap][i] = null;
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
                gamePanel.npc[gamePanel.currentMap][i].speak();
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
//        drawDebugRects(graphics2D, screenX, screenY);

    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }
}
