package com.vengeance.game.object;

import com.vengeance.game.Animation;
import com.vengeance.game.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Key extends SuperObject {
    public Key(GamePanel gp) {
        super(gp);
        name = Object.KEY;
        try {
            int tileSize = 32;
            BufferedImage fullTileImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resources/images/objects/Key.png")));
            image = fullTileImage.getSubimage(0, 0, tileSize, tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
