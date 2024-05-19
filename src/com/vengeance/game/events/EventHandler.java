package com.vengeance.game.events;

import com.vengeance.game.entity.Entity;
import com.vengeance.game.main.GamePanel;

public class EventHandler {
    GamePanel gamePanel;
    EventRect[][][] eventMatrix;

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp) {
        gamePanel = gp;

        eventMatrix = new EventRect[gamePanel.maxMap][gamePanel.getMaxWorldColumns()][gamePanel.getMaxWorldRows()];

        int map = 0;
        int col = 0;
        int row = 0;

        while (col < gamePanel.getMaxWorldColumns() && row < gamePanel.getMaxWorldRows()) {
            eventMatrix[map][col][row] = new EventRect();
            eventMatrix[map][col][row].x = 20;
            eventMatrix[map][col][row].y = 12;
            eventMatrix[map][col][row].width = 2;
            eventMatrix[map][col][row].height = 2;
            eventMatrix[map][col][row].eventRectDefaultX = eventMatrix[map][col][row].x;
            eventMatrix[map][col][row].eventRectDefaultY = eventMatrix[map][col][row].y;

            col++;
            if (col == gamePanel.getMaxWorldColumns()) {
                col = 0;
                row++;
                if (row == gamePanel.getMaxWorldRows()) {
                    row = 0;
                    map++;
                    if (map == gamePanel.maxMap) {
                        break;
                    }
                }
            }
        }
    }

    // Called in update
    public void checkEvent() {

        int xDistance = Math.abs(gamePanel.player.worldX - previousEventX);
        int yDistance = Math.abs(gamePanel.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > gamePanel.player.getDrawHeight()) {
            canTouchEvent = true;
        }

        int[][] pitCoords = {
                {30, 35},
                {4, 9},
                {34, 41},
                {12, 25},
                {4, 18},
                {6, 33},
                {5, 42},
                {17, 40},
                {38, 26},
                {30, 22},
                {37, 11},
                {25, 9},
                {18, 10},
        };
        for (int[] coord : pitCoords) {
            if (canTouchEvent && hit(0, coord[0], coord[1], null)) {
                // event happens
                damagePit(GamePanel.GAME_STATE.DIALOG_STATE);
            }
        }
//        if (canTouchEvent && hit(0, 15, 12, null)) {
//            heal(GamePanel.GAME_STATE.DIALOG_STATE);
//        }
        if (hit(0, 11, 6, null)) {
            if (gamePanel.player.keysGathered >= 8) {
                teleportEvent(1, 33, 38);
            } else {
                gamePanel.ui.setMessage("Not enough keys gathered!");
            }
        }
        if (hit(1, 38, 42, null)) {
            teleportEvent(0, 11, 6);
        } else if (hit(1, 12, 6, null)) {
            teleportEvent(2, 5, 39);
        } else if (hit(2, 7, 42, null)) {
            teleportEvent(1, 11, 5);
        }
    }

    public void teleport(int map, int x, int y) {
        gamePanel.currentMap = map;
        gamePanel.player.worldX = gamePanel.getTileSize() * x;
        gamePanel.player.worldY = gamePanel.getTileSize() * y;
        previousEventX = gamePanel.player.worldX;
        previousEventY = gamePanel.player.worldY;
        canTouchEvent = false;
        gamePanel.keyHandler.ePressed = false;
    }

    public void teleportEvent(int map, int x, int y) {
        gamePanel.ui.setMessage("Press E to change room");
        if (gamePanel.keyHandler.ePressed) {
            gamePanel.playSE(4);
            teleport(map, x, y);
        }
    }

    public boolean hit(int map, int col, int row, Entity.direction reqDirection) {
        // reqDirection: null if any direction is ok
        boolean hit = false;
        if (map == gamePanel.currentMap) {
            gamePanel.player.collisionArea.x = gamePanel.player.worldX + gamePanel.player.collisionArea.x;
            gamePanel.player.collisionArea.y = gamePanel.player.worldY + gamePanel.player.collisionArea.y;
            eventMatrix[map][col][row].x = col * gamePanel.getTileSize() + eventMatrix[map][col][row].x;
            eventMatrix[map][col][row].y = row * gamePanel.getTileSize() + eventMatrix[map][col][row].y;

            if (gamePanel.player.collisionArea.intersects(eventMatrix[map][col][row]) && !eventMatrix[map][col][row].eventDone) {
                if (gamePanel.player.walkDirection == reqDirection || reqDirection == null) {
//                eventMatrix[col][row].eventDone = true;
                    hit = true;

                    previousEventX = gamePanel.player.worldX;
                    previousEventY = gamePanel.player.worldY;
                }
            }

            gamePanel.player.collisionArea.x = gamePanel.player.solidAreaDefaultX;
            gamePanel.player.collisionArea.y = gamePanel.player.solidAreaDefaultY;
            eventMatrix[map][col][row].x = eventMatrix[map][col][row].eventRectDefaultX;
            eventMatrix[map][col][row].y = eventMatrix[map][col][row].eventRectDefaultY;
        }
        return hit;
    }

    public void damagePit(GamePanel.GAME_STATE gameState) {
        gamePanel.gameState = gameState;
        gamePanel.ui.currentDialog = "You fell into a pit!";
        gamePanel.player.life--;
//        eventMatrix[col][row].eventDone = true;
        canTouchEvent = false;
    }

    public void heal(GamePanel.GAME_STATE gameState) {
        if (gamePanel.keyHandler.enterPressed) {
            gamePanel.gameState = gameState;
            gamePanel.ui.currentDialog = "You heal!";
            gamePanel.player.life = gamePanel.player.maxLife;
        }
        gamePanel.keyHandler.enterPressed = false;
    }
}
