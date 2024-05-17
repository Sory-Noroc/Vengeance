package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;
import com.vengeance.game.main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.vengeance.game.entity.Entity.direction.WALK_RIGHT;

public class FireWarrior extends Enemy {

    public FireWarrior(GamePanel panel) {
        super(panel);

        spriteScale = gamePanel.getScale() - 2;
        totalSprites = 8;

        width = 144;
        height = 80;
        drawWidth = 112 * spriteScale;
        drawHeight = height * spriteScale;

        speed = 2;
        maxLife = 3;
        life = maxLife;
        setCollisionArea(new Rectangle(
                42 * spriteScale,
                32 * spriteScale,
                30 * spriteScale,
                31 * spriteScale
        ));
        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;

        setDirection(WALK_RIGHT);
        setDrawDirection(WALK_RIGHT);
        width = 112;
        setAttackArea(36, 15);
        width = 144;

        setImages(direction.WALK_RIGHT, "/resources/images/enemies/Fire_Warrior.png", width, height, 8, 2*height);
        setImages(direction.WALK_LEFT, "/resources/images/enemies/leftFire_Warrior.png", width, height, 8, 2*height);
        setImages(direction.ATTACK_LEFT, "/resources/images/enemies/leftFire_Warrior.png", width, height, 5, 10*height);
        setImages(direction.ATTACK_RIGHT, "/resources/images/enemies/Fire_Warrior.png", width, height, 5, 10*height);
    }

    @Override
    protected void setHealthBarPos(int screenX, int screenY) {
        super.setHealthBarPos(screenX, screenY);
        hpX += 40;
        hpY -= 60;
    }

    @Override
    protected void loadSubImages(BufferedImage[] dir, BufferedImage sprite, int startY) {
        if (dir == walkRight || dir == attackRight) {
            int index = 0;
            for (int i = 0; i < totalSprites; i++) {
                dir[index] = sprite.getSubimage(width * i, startY, 112, height);
                dir[index] = UtilityTool.scaleImage(dir[index], drawWidth, drawHeight);
                index++;
            }
        } else {
            int index = 0;
            for (int i = 0; i < totalSprites; i++) {
                dir[index] = sprite.getSubimage(width * i + width - 112, startY, 112, height);
                dir[index] = UtilityTool.scaleImage(dir[index], drawWidth, drawHeight);
                index++;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);

        // Health Bar
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        double oneScale = (double)healthBarWidth/maxLife;
        double hpValue = oneScale * life;
        g.setColor(new Color(35, 35, 35));
        g.fillRect(hpX, hpY, healthBarWidth, healthBarHeight);
        g.setColor(new Color(255, 0, 30));
        g.fillRect(hpX, hpY, (int)hpValue, healthBarHeight);
    }
}
