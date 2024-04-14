package com.vengeance.game.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.vengeance.game.GamePanel;
import com.vengeance.game.KeyHandler;

public class Player extends Entity {

    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;

    private final int screenX;
    private final int screenY;
    private final int drawWidth;
    private final int drawHeight;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        width = 52;
        height = 50;
        drawWidth = width * gamePanel.getScale();
        drawHeight = height * gamePanel.getScale();
        this.screenX = gamePanel.getScreenWidth() / 2 - (drawWidth / 2);
        this.screenY = gamePanel.getScreenHeight() / 2 - (drawHeight / 2);

        setCollisionArea(new Rectangle(
                16 * gamePanel.getScale(),
                24 * gamePanel.getScale(),
                21 * gamePanel.getScale(),
                19 * gamePanel.getScale()));

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        setWorldX(gamePanel.getTileSize() * 25);
        setWorldY(gamePanel.getTileSize() * 21);
        setSpeed(4);
        setDirection("down");
    }

    public void getPlayerImage() {
         setImages("right", "/resources/images/player/vikingPlayer/walkSheetRight.png", width, height, 2, 3);
         setImages("left", "/resources/images/player/vikingPlayer/walkSheetLeft.png", width, height, 2, 3);
    }

    @Override
    public void update() {

        if (keyHandler.isUpPressed() || keyHandler.isDownPressed()
                || keyHandler.isLeftPressed() || keyHandler.isRightPressed()) {

            if (keyHandler.isUpPressed()) {
                setDirection("up");
            } else if (keyHandler.isDownPressed()) {
                setDirection("down");
            } else if (keyHandler.isLeftPressed()) {
                setDirection("left");
            } else if (keyHandler.isRightPressed()) {
                setDirection("right");
            }

            checkCollision();
            moveIfCollisionNotDetected();
            checkAndChangeSpriteAnimation();
        }
    }

    private void checkCollision() {
        setCollisionOn(false);
        gamePanel.getCollisionChecker().checkTile(this);
    }

    private void moveIfCollisionNotDetected() {
        if (!isCollisionOn()) {
            switch (getDirection()) {
                case "up" -> setWorldY(getWorldY() - getSpeed());
                case "down" -> setWorldY(getWorldY() + getSpeed());
                case "left" -> setWorldX(getWorldX() - getSpeed());
                case "right" -> setWorldX(getWorldX() + getSpeed());
            }
        }
    }

    private void checkAndChangeSpriteAnimation() {
        setSpriteCounter(getSpriteCounter() + 1);
        if (getSpriteCounter() > 6) {

            switch (getSpriteNumber()) {
                case 1: setSpriteNumber(2); break;
                case 2: setSpriteNumber(3); break;
                case 3: setSpriteNumber(4); break;
                case 4: setSpriteNumber(5); break;
                case 5: setSpriteNumber(6); break;
                case 6: setSpriteNumber(1); break;
            }
            setSpriteCounter(0);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(getDirectionalImage(), screenX, screenY, drawWidth, drawHeight, null);
    }

    private BufferedImage getDirectionalImage() {
        BufferedImage image = null;

        switch (getDirection()) {
            case "left" -> {
                switch(getSpriteNumber()) {
                    case 1: image = getLeft()[0]; break;
                    case 2: image = getLeft()[1]; break;
                    case 3: image = getLeft()[2]; break;
                    case 4: image = getLeft()[3]; break;
                    case 5: image = getLeft()[4]; break;
                    case 6: image = getLeft()[5]; break;
                }
            }
            default -> {
                switch (getSpriteNumber()) {
                    case 1: image = getRight()[0]; break;
                    case 2: image = getRight()[1]; break;
                    case 3: image = getRight()[2]; break;
                    case 4: image = getRight()[3]; break;
                    case 5: image = getRight()[4]; break;
                    case 6: image = getRight()[5]; break;
                }
            }
        }
        return image;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }
}
