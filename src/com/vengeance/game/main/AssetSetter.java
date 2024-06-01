package com.vengeance.game.main;

import com.vengeance.game.entity.Boss;
import com.vengeance.game.entity.FireWarrior;
import com.vengeance.game.entity.Warrior;
import com.vengeance.game.entity.KingNPC;
import com.vengeance.game.object.*;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;

    }

    public void setObjects() {

        int mapNum = 0;
        int[][] keyCoords = {
                {20, 10},
                {10, 10},
                {20, 40},
                {40, 41},
                {40, 10},
                {5, 40},
                {2, 20},
                {15, 27}
        };

//        gp.obj[mapNum][0] = new Chest(gp);
//        gp.obj[mapNum][0].worldX = 14 * gp.getTileSize();
//        gp.obj[mapNum][0].worldY = 14 * gp.getTileSize();

        for (int i = 1; i <= keyCoords.length; i++) {
            gp.obj[mapNum][i] = new Key(gp);
            gp.obj[mapNum][i].worldX = keyCoords[i-1][0] * gp.getTileSize();
            gp.obj[mapNum][i].worldY = keyCoords[i-1][1] * gp.getTileSize();
        }
    }

    public void setNPCs() {
        int mapNum = 1;
        gp.npc[mapNum][0] = new KingNPC(gp);
        gp.npc[mapNum][0].worldX = 2 * gp.getTileSize();
        gp.npc[mapNum][0].worldY = 7 * gp.getTileSize();
    }

    public void setEnemies() {
        int mapNum = 0;
        int[][][] enemyCoords = {
                {
                        {10, 10},
                        {28, 7},
                },

                {
                        {5, 9},
                        {20, 10},
                        {30, 8},
                        {40, 10},
                        {3, 24},
                        {10, 26},
                        {30, 25},
                        {10, 39},
                        {25, 40},
                },
                {
                        {24, 11}
                }

        };

        for (int i = 0; i < enemyCoords[mapNum].length; i++) {
            gp.enemy[mapNum][i] = new Warrior(gp);
            gp.enemy[mapNum][i].worldX = gp.getTileSize() * enemyCoords[mapNum][i][0];
            gp.enemy[mapNum][i].worldY = gp.getTileSize() * enemyCoords[mapNum][i][1];
        }

        mapNum = 1;
        // Enemies in second map
        for (int i = 0; i < enemyCoords[mapNum].length; i++) {
            gp.enemy[mapNum][i] = new FireWarrior(gp);
            gp.enemy[mapNum][i].worldX = gp.getTileSize() * enemyCoords[mapNum][i][0];
            gp.enemy[mapNum][i].worldY = gp.getTileSize() * enemyCoords[mapNum][i][1];
        }

        mapNum = 2;
        // Boss fight
        gp.enemy[mapNum][0] = new Boss(gp);
        gp.enemy[mapNum][0].worldX = gp.getTileSize() * enemyCoords[mapNum][0][0];
        gp.enemy[mapNum][0].worldY = gp.getTileSize() * enemyCoords[mapNum][0][1];
    }
}
