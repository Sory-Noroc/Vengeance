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
    private final int spriteScale;
    private int totalSprites = 8;

    public int keysGathered = 0;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        super(gamePanel);
        this.keyHandler = keyHandler;
        spriteScale = gamePanel.getScale() - 2;

        width = 115;
        height = 84;
        drawWidth = width * spriteScale;
        drawHeight = height * spriteScale;
        this.screenX = gamePanel.getScreenWidth() / 2 - (drawWidth / 2);
        this.screenY = gamePanel.getScreenHeight() / 2 - (drawHeight / 2);

        setCollisionArea(new Rectangle(
                42 * spriteScale,
                45 * spriteScale,
                32 * spriteScale,
                26 * spriteScale));

        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        setWorldX(gamePanel.getTileSize() * 25);
        setWorldY(gamePanel.getTileSize() * 21);
        setSpeed(4);
        setDirection(WALK_RIGHT);
        setDrawDirection(WALK_RIGHT);
    }

    public Entity setImages(direction direction, String imagePath, int width, int height, int count, int startY) {
        BufferedImage sprite = null;
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (IOException e) {
            e.printStackTrace();
            exit(-1);
        }

        BufferedImage[] dir = null;
        switch (direction) {
            case WALK_LEFT: {
                walkLeft = new BufferedImage[count];
                dir = walkLeft;
                break;
            }
            case WALK_RIGHT: {
                walkRight = new BufferedImage[count];
                dir = walkRight;
                break;
            }
            case IDLE_RIGHT: {
                idleRight = new BufferedImage[count];
                dir = idleRight;
                break;
            }
            case IDLE_LEFT: {
                idleLeft = new BufferedImage[count];
                dir = idleLeft;
                break;
            }
        }
        int index = 0;
        for (int i = 0; i < count; i++) {
            dir[index] = sprite.getSubimage(width * i, startY + height, width, height);
            dir[index] = UtilityTool.scaleImage(dir[index], drawWidth, drawHeight);
            index++;
        }
        return this;
    }

    public BufferedImage getPlayerImage() {
         setImages(direction.WALK_RIGHT, "/resources/images/player/Viking-Sheet.png", width, height, 8, height);
         setImages(direction.WALK_LEFT, "/resources/images/player/LeftViking-Sheet.png", width, height, 8, height);
         setImages(direction.IDLE_LEFT, "/resources/images/player/LeftViking-Sheet.png", width, height, 8, 0);
         setImages(direction.IDLE_RIGHT, "/resources/images/player/Viking-Sheet.png", width, height, 8, 0);
         return getWalkRight()[1];
    }

    @Override
    public void update() {

        if (
                keyHandler.isUpPressed() ||
                keyHandler.isDownPressed() ||
                keyHandler.isLeftPressed() ||
                keyHandler.isRightPressed())
        {
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
        int objIndex = gamePanel.getCollisionChecker().checkObject(this, true);
        interactWithObject(objIndex);

        if (!isCollisionOn()) {
            switch (getDirection()) {
                case WALK_UP -> setWorldY(getWorldY() - getSpeed());
                case WALK_DOWN -> setWorldY(getWorldY() + getSpeed());
                case WALK_LEFT -> setWorldX(getWorldX() - getSpeed());
                case WALK_RIGHT -> setWorldX(getWorldX() + getSpeed());
            }
        }
    }

    private void checkAndChangeSpriteAnimation() {
        setSpriteCounter(getSpriteCounter() + 1);
        if (getSpriteCounter() > 6) {

            if (getSpriteNumber() < totalSprites) {
                setSpriteNumber(getSpriteNumber() + 1);
            } else {
                setSpriteNumber(1);
            }
            setSpriteCounter(0);
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

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(getDirectionalImage(), screenX, screenY, null);
    }

    private BufferedImage getDirectionalImage() {
        BufferedImage image = null;

        switch (getDrawDirection()) {
            case WALK_LEFT -> {
                image = getWalkLeft()[getSpriteNumber() - 1];
            }
            case WALK_RIGHT -> {
                image = getWalkRight()[getSpriteNumber() - 1];
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
