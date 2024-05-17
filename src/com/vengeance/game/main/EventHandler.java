package com.vengeance.game.main;

import com.vengeance.game.entity.Entity;

public class EventHandler {
    GamePanel gamePanel;
    EventRect[][] eventMatrix;

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp) {
        gamePanel = gp;

        eventMatrix = new EventRect[gamePanel.getMaxWorldColumns()][gamePanel.getMaxWorldRows()];

        int col = 0;
        int row = 0;

        while (col < gamePanel.getMaxWorldColumns() && row < gamePanel.getMaxWorldRows()) {
            eventMatrix[col][row] = new EventRect();
            eventMatrix[col][row].x = 20;
            eventMatrix[col][row].y = 12;
            eventMatrix[col][row].width = 2;
            eventMatrix[col][row].height = 2;
            eventMatrix[col][row].eventRectDefaultX = eventMatrix[col][row].x;
            eventMatrix[col][row].eventRectDefaultY = eventMatrix[col][row].y;

            col++;
            if (col == gamePanel.getMaxWorldColumns()) {
                col = 0;
                row++;
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
        if (canTouchEvent && hit(4, 9, Entity.direction.WALK_LEFT)) {
            // event happens
            damagePit(4, 9, GamePanel.GAME_STATE.DIALOG_STATE);
        }
        if (canTouchEvent && hit(15, 12, null)) {
            heal(GamePanel.GAME_STATE.DIALOG_STATE);
        }
    }

    public boolean hit(int col, int row, Entity.direction reqDirection) {
        // reqDirection: null if any direction is ok
        boolean hit = false;
        gamePanel.player.collisionArea.x = gamePanel.player.worldX + gamePanel.player.collisionArea.x;
        gamePanel.player.collisionArea.y = gamePanel.player.worldY + gamePanel.player.collisionArea.y;
        eventMatrix[col][row].x = col * gamePanel.getTileSize() + eventMatrix[col][row].x;
        eventMatrix[col][row].y = row * gamePanel.getTileSize() + eventMatrix[col][row].y;

        if (gamePanel.player.collisionArea.intersects(eventMatrix[col][row]) && !eventMatrix[col][row].eventDone) {
            if (gamePanel.player.walkDirection == reqDirection || reqDirection == null) {
//                eventMatrix[col][row].eventDone = true;
                hit = true;

                previousEventX = gamePanel.player.worldX;
                previousEventY = gamePanel.player.worldY;
            }
        }

        gamePanel.player.collisionArea.x = gamePanel.player.solidAreaDefaultX;
        gamePanel.player.collisionArea.y = gamePanel.player.solidAreaDefaultY;
        eventMatrix[col][row].x = eventMatrix[col][row].eventRectDefaultX;
        eventMatrix[col][row].y = eventMatrix[col][row].eventRectDefaultY;
        return hit;
    }

    public void damagePit(int col, int row, GamePanel.GAME_STATE gameState) {
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
