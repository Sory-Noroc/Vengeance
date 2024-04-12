package com.vengeance.game.tile;

import com.vengeance.game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static java.lang.System.exit;
import static java.lang.System.out;

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

    public void getTileImage() {
        try {
            BufferedImage fullTileImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resources/images/tiles/castleTiles.png")));

            for (int i = 0; i < tileCount; i++) {
                tiles[i+1] = new Tile();
                tiles[i+1].setImage(fullTileImage.getSubimage((i % 16) * tileSize, i / 16 * tileSize, tileSize, tileSize));
                tiles[i+1].setCollision(true);
            }
            tiles[130].setCollision(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String mapPath) {
        try {
            int column = 0;
            int row = 0;
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
                    int number = Integer.parseInt(mapString[row * gamePanel.getMaxWorldRows() + column]);

                    mapTileNumbers[row][column] = number;
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

            int worldX = worldColumn * gamePanel.getTileSize();
            int worldY = worldRow * gamePanel.getTileSize();
            int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
            int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY();

            if (worldX + gamePanel.getTileSize() > gamePanel.getPlayer().getWorldX() - gamePanel.getPlayer().getScreenX() &&
                    worldX - gamePanel.getTileSize() < gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX() &&
                    worldY + gamePanel.getTileSize() > gamePanel.getPlayer().getWorldY() - gamePanel.getPlayer().getScreenY() &&
                    worldY - gamePanel.getTileSize() < gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY()) {

                graphics2D.drawImage(tiles[tileNumber].getImage(), screenX, screenY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
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