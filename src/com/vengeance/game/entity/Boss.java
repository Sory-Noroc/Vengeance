package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;
import java.awt.*;
import static com.vengeance.game.entity.Entity.direction.*;

public class Boss extends Enemy {

    public Boss(GamePanel gp) {
        super(gp);

        visible = true;
        spriteScale = gamePanel.getScale() - 2;
        totalSprites = 8;
        damage = 4;

        width = 94;
        height = 91;
        drawWidth = width * spriteScale;
        drawHeight = height * spriteScale;

        speed = 2;
        maxLife = 20;
        life = maxLife;
        setCollisionArea(new Rectangle(
                23 * spriteScale,
                40 * spriteScale,
                40 *spriteScale,
                50 *spriteScale
        ));
        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;

        setDirection(WALK_RIGHT);
        setDrawDirection(WALK_RIGHT);

        setSprites();
    }

    protected void setSprites() {
        setImages(WALK_RIGHT, "/resources/images/enemies/boss.png", width, height, 8, height);
        setImages(WALK_LEFT, "/resources/images/enemies/leftboss.png", width, height, 8, height);
        setImages(ATTACK_RIGHT, "/resources/images/enemies/boss.png", width, height, 13, 0);
        setImages(ATTACK_LEFT, "/resources/images/enemies/leftboss.png", width, height, 13, 0);
    }

    @Override
    protected void moveIfCollisionNotDetected() {
        totalSprites = 8;
        super.moveIfCollisionNotDetected();
    }

    @Override
    public void update() {
        super.update();
        if (life <= 0) {
            gamePanel.gameState = GamePanel.GAME_STATE.WON_STATE;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        Player player = gamePanel.getPlayer();
        int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.getScreenX();
        int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.getScreenY();
        setHealthBarPos(screenX, screenY);

        if (worldX + player.getDrawWidth() > player.getWorldX() - player.getScreenX() &&
                worldY + player.getDrawHeight() > player.getWorldY() - player.getScreenY() &&
                worldX - player.getDrawWidth() < player.getWorldX() + player.getScreenX() &&
                worldY - player.getDrawHeight() < player.getWorldY() + player.getScreenY() ) {

            if (invincible) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            } else {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
            g.drawImage(getDirectionalImage(), screenX, screenY, drawWidth, drawHeight, null);
            drawHealthBar(g);
        }
    }
}
