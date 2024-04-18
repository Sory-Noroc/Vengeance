package com.vengeance.game.object;

import com.vengeance.game.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Chest extends SuperObject {
    public Chest(GamePanel gamePanel) {
        super(gamePanel);
        name = Object.CHEST;
        width = 2;
        height = 2;
        try {
            int tileSize = 8;
            BufferedImage fullTileImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resources/images/tiles/castleTiles.png")));
            image = fullTileImage.getSubimage(12 * tileSize, 9 * tileSize, tileSize*width, tileSize*height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
