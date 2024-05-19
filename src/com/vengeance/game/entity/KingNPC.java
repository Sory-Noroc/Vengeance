package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static com.vengeance.game.entity.Entity.direction.*;

public class KingNPC extends MobileEntity {

    public KingNPC(GamePanel gp) {
        super(gp);
        drawDirection = WALK_RIGHT;
        walkDirection = WALK_RIGHT;
        setCollisionOn(false);
        width = 231;
        height = 190;
        collisionArea = new Rectangle(83,56,55,85);
        solidAreaDefaultX = 83;
        solidAreaDefaultY = 56;
        drawWidth = width;
        drawHeight = height;
        speed = 2;
        setImages();
        setDialogue();
    }

    public void setDialogue() {
        dialogues[0] = "I knew I can count on you...";
        dialogues[1] = "Basim has destroyed our army and plans on \nbringing chaos to our kingdom.";
        dialogues[2] = "Only you can stop him!";
        dialogues[3] = "He is in the throne room on your right.";
    }

    public void setWinningDialogues() {
        Arrays.fill(dialogues, null);
        dialogues[0] = "I knew you were worthy son!";
    }

    private void setImages() {
        setImages(direction.WALK_RIGHT, "/resources/images/npcs/kingSpritesheet.png", width, height, 6, 0);
    }

    @Override
    public void setAction() {}

    @Override
    protected BufferedImage getDirectionalImage() {
        return getWalkRight()[getSpriteNumber() - 1];
    }

    @Override
    protected void moveIfCollisionNotDetected() {}

    @Override
    protected void checkCollision() {
        super.checkCollision();
        gamePanel.getCollisionChecker().checkPlayer(this);
    }

    @Override
    public void update() {
        super.update();
        if (gamePanel.gameState == GamePanel.GAME_STATE.WON_STATE) {
            setWinningDialogues();
        }
    }
}
