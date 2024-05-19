package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;
import com.vengeance.game.main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import static com.vengeance.game.entity.Entity.direction.*;
import static java.lang.System.exit;


public abstract class MobileEntity extends Entity {

    int damage = 1;
    public int hpX, hpY;
    protected int healthBarWidth = 100, healthBarHeight = 10;
    protected int totalSprites;
    protected boolean visible = true;
    int spriteScale;
    protected int clock;
    String[] dialogues = new String[20];
    protected int dialogueIndex = 0;
    protected boolean invincible = false;
    protected int invincibleCount = 0;
    protected boolean isAttacking = false;

    public MobileEntity(GamePanel gamePanel) {
        super(gamePanel);
    }

    protected void setHealthBarPos(int screenX, int screenY) {
        hpX = screenX + (width - healthBarWidth) / 2;
        hpY = screenY + height;
    }

    protected void hit() {
        if (!invincible) {
            life--;
            invincible = true;
        }
    }

    public void setAttacking(boolean attacking) {
        totalSprites = attacking ? 4 : 8;
        isAttacking = attacking;
        setSpriteNumber(1);
        setSpriteCounter(0);
    }

    protected void speak() {
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gamePanel.ui.currentDialog = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gamePanel.player.walkDirection) {
            case WALK_LEFT -> drawDirection = WALK_RIGHT;
            case WALK_RIGHT -> drawDirection = WALK_LEFT;
        }
    }

    public void setAction() {
        clock++;
        if (clock == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            if (i <= 25) {
                setDirection(WALK_UP);
            } else if (i <= 50) {
                setDirection(WALK_DOWN);
            } else if (i <= 75) {
                setDirection(WALK_LEFT);
                setDrawDirection(WALK_LEFT);
            } else {
                setDirection(WALK_RIGHT);
                setDrawDirection(WALK_RIGHT);
            }
            clock = 0;
        }
    }

    @Override
    public void setImages(direction direction, String imagePath, int width, int height, int count, int startY) {
        BufferedImage sprite = null;
        try {
            totalSprites = count;
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
            case ATTACK_LEFT: {
                attackLeft = new BufferedImage[count];
                dir = attackLeft;
                break;
            }
            case ATTACK_RIGHT: {
                attackRight = new BufferedImage[count];
                dir = attackRight;
                break;
            }
        }
        loadSubImages(dir, sprite, startY);
    }

    protected void loadSubImages(BufferedImage[] dir, BufferedImage sprite, int startY) {
        int index = 0;
        for (int i = 0; i < totalSprites; i++) {
            dir[index] = sprite.getSubimage(width * i, startY, width, height);
            dir[index] = UtilityTool.scaleImage(dir[index], drawWidth, drawHeight);
            index++;
        }
    }

    protected BufferedImage getDirectionalImage() {
        BufferedImage image = null;

        switch (getDrawDirection()) {
            case WALK_LEFT -> image = isAttacking ? getAttackLeft()[getSpriteNumber() - 1] : getWalkLeft()[getSpriteNumber() - 1];
            case WALK_RIGHT -> image = isAttacking ? getAttackRight()[getSpriteNumber() - 1] : getWalkRight()[getSpriteNumber() - 1];
        }
        return image;
    }

    protected void checkCollision() {
        setCollisionOn(false);
        gamePanel.getCollisionChecker().checkTile(this);
        gamePanel.getCollisionChecker().checkEntity(this, gamePanel.npc);
    }

    protected void moveIfCollisionNotDetected() {
        if (!isCollisionOn()) {
            switch (getDirection()) {
                case WALK_UP -> setWorldY(getWorldY() - getSpeed());
                case WALK_DOWN -> setWorldY(getWorldY() + getSpeed());
                case WALK_LEFT -> setWorldX(getWorldX() - getSpeed());
                case WALK_RIGHT -> setWorldX(getWorldX() + getSpeed());
            }
        }
    }

    protected void checkAndChangeSpriteAnimation() {
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

    protected void updateInvincible() {
        if (invincible) {
            invincibleCount++;
            if (invincibleCount > 60) {
                invincible = false;
                invincibleCount = 0;
            }
        }
    }

    protected void collisionSpriteAndMovement() {
        checkCollision();
        moveIfCollisionNotDetected();
        checkAndChangeSpriteAnimation();
    }

    protected void attackAnimation() {
        setSpriteCounter(getSpriteCounter() + 1);
        if (getSpriteCounter() > 4) {
            if (getSpriteNumber() < totalSprites) {
                if (getSpriteNumber() == 1) {
                    gamePanel.playSE(3);
                }
                setSpriteNumber(getSpriteNumber() + 1);
            } else {
                // spriteNumber is more than 4
                setAttacking(false);
            }
            setSpriteCounter(0);
        }
    }

    @Override
    public void update() {
        setAction();
        collisionSpriteAndMovement();
        updateInvincible();
    }

//    public void drawDebugRects(Graphics2D g, int screenX, int screenY) {
//        g.setColor(Color.BLACK);
//        g.drawRect(screenX + collisionArea.x, screenY + collisionArea.y, collisionArea.width, collisionArea.height);
//    }

    @Override
    public void draw(Graphics2D g) {
        Player player = gamePanel.getPlayer();
        int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.getScreenX();
        int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.getScreenY();
        setHealthBarPos(screenX, screenY);

        visible = false;
        if (worldX + player.getDrawWidth() > player.getWorldX() - player.getScreenX() &&
                worldY + player.getDrawHeight() > player.getWorldY() - player.getScreenY() &&
                worldX - player.getDrawWidth() < player.getWorldX() + player.getScreenX() &&
                worldY - player.getDrawHeight() < player.getWorldY() + player.getScreenY() ) {
            // If visible
            visible = true;
            if (invincible) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            } else {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
            g.drawImage(getDirectionalImage(), screenX, screenY, drawWidth, drawHeight, null);
//            drawDebugRects(g, screenX, screenY);
            //
        }
    }
}
