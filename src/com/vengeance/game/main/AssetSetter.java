package com.vengeance.game.main;

import com.vengeance.game.object.*;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;

    }

    public void setObject() {
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
}
