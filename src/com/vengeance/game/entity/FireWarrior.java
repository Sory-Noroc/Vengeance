package com.vengeance.game.entity;

import com.vengeance.game.main.GamePanel;

public class FireWarrior extends Warrior {
    public FireWarrior(GamePanel gp) {
        super(gp);
        maxLife = 2;
        life = 2;
        speed = 3;
    }

    @Override
    protected void setSprites() {
        setImages(direction.WALK_RIGHT, "/resources/images/enemies/FireWarrior.png", width, height, 8, 2*height);
        setImages(direction.WALK_LEFT, "/resources/images/enemies/leftFireWarrior.png", width, height, 8, 2*height);
        setImages(direction.ATTACK_LEFT, "/resources/images/enemies/leftFireWarrior.png", width, height, 5, 10*height);
        setImages(direction.ATTACK_RIGHT, "/resources/images/enemies/FireWarrior.png", width, height, 5, 10*height);
    }
}
