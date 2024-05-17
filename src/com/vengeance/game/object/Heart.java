package com.vengeance.game.object;

import com.vengeance.game.entity.Entity;
import com.vengeance.game.main.GamePanel;
import com.vengeance.game.main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Heart extends SuperObject {
    public BufferedImage heartFull, heartEmpty, heartHalf;
    int tileSize = 32;
    public int drawSize = tileSize * 3;

    public Heart(GamePanel panel) {
        super(panel);
        name = Object.HEART;
        try {
            BufferedImage fullTileImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resources/images/objects/hearts.png")));
            heartFull = scale(fullTileImage.getSubimage(0, 0, tileSize, tileSize));
            heartHalf = scale(fullTileImage.getSubimage(32, 0, tileSize, tileSize));
            heartEmpty = scale(fullTileImage.getSubimage(64, 0, tileSize, tileSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }

    private BufferedImage scale(BufferedImage image) {
        return UtilityTool.scaleImage(image.getSubimage(0, 0, tileSize, tileSize), drawSize, drawSize);
    }
}
