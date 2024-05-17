package com.vengeance.game.main;

import com.vengeance.game.entity.FireWarrior;
import com.vengeance.game.entity.KingNPC;
import com.vengeance.game.object.*;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;

    }

    public void setObjects() {
        int[][] keyCoords = {
                {20, 10},
                {10, 10},
                {20, 40},
                {40, 41},
                {40, 10}
        };

        gp.obj[0] = new Chest(gp);
        gp.obj[0].worldX = 14 * gp.getTileSize();
        gp.obj[0].worldY = 14 * gp.getTileSize();

        for (int i = 1; i <= keyCoords.length; i++) {
            gp.obj[i] = new Key(gp);
            gp.obj[i].worldX = keyCoords[i-1][0] * gp.getTileSize();
            gp.obj[i].worldY = keyCoords[i-1][1] * gp.getTileSize();
        }
    }

    public void setNPCs() {
        gp.npc[0] = new KingNPC(gp);
        gp.npc[0].worldX = 18 * gp.getTileSize();
        gp.npc[0].worldY = 37 * gp.getTileSize();
    }

    public void setEnemies() {
        int[][] enemyCoords = {
                {10, 10},
                {28,  7},
                {20, 40},
                {40, 38},
                {40, 10}
        };
        for (int i = 0; i < enemyCoords.length; i++) {
            gp.enemy[i] = new FireWarrior(gp);
            gp.enemy[i].worldX = gp.getTileSize() * enemyCoords[i][0];
            gp.enemy[i].worldY = gp.getTileSize() * enemyCoords[i][1];
        }
    }
}
