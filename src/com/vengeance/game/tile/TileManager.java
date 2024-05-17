package com.vengeance.game.tile;

import com.vengeance.game.AnimationFactory;
import com.vengeance.game.main.GamePanel;
import com.vengeance.game.entity.Player;
import com.vengeance.game.main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

import static java.lang.System.exit;

public class TileManager {

    private final GamePanel gamePanel;

    private final int tileCount = 256;
    private final int tileSize = 8;
    private final Tile[] tiles;
    private final int[][] mapTileNumbers;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tiles = new Tile[tileCount + 1];
        this.mapTileNumbers = new int[gamePanel.getMaxWorldColumns()][gamePanel.getMaxWorldRows()];

        getTileImage();
        loadMap("/resources/maps/level1.txt");
    }

    public void setup(int index, BufferedImage image, boolean collision) {
        try {
            tiles[index] = new Tile();
            tiles[index].setImage(UtilityTool.scaleImage(image, gamePanel.getTileSize(), gamePanel.getTileSize()));
            tiles[index].setCollision(collision);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTileImage() {
        try {
            int[] falseColliders = {130, 162, 180, 179, 30, 46, 146, 11, 12, 13, 27, 28, 29, 43, 44, 45};
            BufferedImage fullTileImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resources/images/tiles/castleTiles.png")));

            for (int i = 0; i < tileCount; i++) {
                BufferedImage subImg = fullTileImage.getSubimage((i % 16) * tileSize, i / 16 * tileSize, tileSize, tileSize);
                setup(i+1, subImg, true);
                tiles[i+1].setCollision(true);
            }
            for (int a: falseColliders) {
                tiles[a].setCollision(false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String mapPath) {
        try {
            int column = 0;
            int row = 0;
            int[] animatedTileNumbers = {14, 30, 46};

            String line;
            StringBuilder fileContent = new StringBuilder();
            InputStream inputStream = getClass().getResourceAsStream(mapPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }

            String[] mapString = fileContent.toString().split(",");
            while (column < gamePanel.getMaxWorldColumns() && row < gamePanel.getMaxWorldRows()) {

                while (column < gamePanel.getMaxWorldColumns()) {
                    int tileNumber = Integer.parseInt(mapString[row * gamePanel.getMaxWorldRows() + column]);

                    mapTileNumbers[row][column] = tileNumber;
                    for (int i = 0; i < animatedTileNumbers.length; i++) {
                        if (animatedTileNumbers[i] == tileNumber) {
                            tiles[tileNumber].setAnimation(AnimationFactory.createAnimation(
                                    3,
                                    20,
                                    tiles[tileNumber].getImage(),
                                    tiles[tileNumber + 1].getImage(),
                                    tiles[tileNumber + 2].getImage()));
                        }
                    }
                    column++;
                }
                if (column == gamePanel.getMaxWorldColumns()) {
                    column = 0;
                    row++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            exit(-1);
        }
    }

    public void draw(Graphics2D graphics2D) {
        int worldColumn = 0;
        int worldRow = 0;

        while (worldColumn < gamePanel.getMaxWorldColumns() && worldRow < gamePanel.getMaxWorldRows()) {

            int tileNumber = mapTileNumbers[worldRow][worldColumn];
            Player player = gamePanel.getPlayer();

            // x and y for the tile in comparison with whole map
            int worldX = worldColumn * gamePanel.getTileSize();
            int worldY = worldRow * gamePanel.getTileSize();

            // x and y for the tile in comparison to screen top edge
            int screenX = worldX - player.getWorldX() + player.getScreenX();
            int screenY = worldY - player.getWorldY() + player.getScreenY();

            if (worldX + player.getDrawWidth() > player.getWorldX() - player.getScreenX() &&
                worldY + player.getDrawHeight() > player.getWorldY() - player.getScreenY() &&
                worldX - player.getDrawWidth() < player.getWorldX() + player.getScreenX() &&
                worldY - player.getDrawHeight() < player.getWorldY() + player.getScreenY() ) {
                graphics2D.drawImage(tiles[tileNumber].getImage(), screenX, screenY, null);
            }
            worldColumn++;

            if (worldColumn == gamePanel.getMaxWorldColumns()) {
                worldColumn = 0;
                worldRow++;
            }
        }
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public int[][] getMapTileNumbers() {
        return mapTileNumbers;
    }
}
