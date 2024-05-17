package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;

import java.awt.*;

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
        dialogues[0] = "Wassup homie? Want some crack? :P";
        dialogues[1] = "Only you can save us!";
        dialogues[2] = "I knew you were ready!";
        dialogues[3] = "Follow this fire fly to the ancient sword\nExcalibur, it's your only hope to destroy Baki!";
        dialogues[4] = "I knew you were the chosen one!";
        dialogues[5] = "Unde e chilotii mei?";
    }

    private void setImages() {
        setImages(direction.WALK_RIGHT, "/resources/images/npcs/kingSpritesheet.png", width, height, 8, height);
        setImages(direction.WALK_LEFT, "/resources/images/npcs/leftkingSpritesheet.png", width, height, 8, height);
        setImages(direction.IDLE_LEFT, "/resources/images/npcs/leftkingSpritesheet.png", width, height, 6, 0);
        setImages(direction.IDLE_RIGHT, "/resources/images/npcs/kingSpritesheet.png", width, height, 6, 0);
    }

    @Override
    protected void checkCollision() {
        super.checkCollision();
        gamePanel.getCollisionChecker().checkPlayer(this);
    }
}
