package com.vengeance.game.main;
import com.vengeance.game.entity.Entity;

public class CollisionChecker {

    private final GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkTile(Entity entity) {

        // Distance from map to collision area
        int entityLeftWorldX = entity.getWorldX() + entity.getCollisionArea().x;
        int entityRightWorldX = entity.getWorldX() + entity.getCollisionArea().x + entity.getCollisionArea().width;
        int entityTopWorldY = entity.getWorldY() + entity.getCollisionArea().y;
        int entityBottomWorldY = entity.getWorldY() + entity.getCollisionArea().y + entity.getCollisionArea().height;

        int entityLeftCol = entityLeftWorldX / gamePanel.getTileSize();
        int entityMiddleCol = (entityLeftWorldX + entityRightWorldX) / 2 / gamePanel.getTileSize();
        int entityRightCol = entityRightWorldX / gamePanel.getTileSize();
        int entityTopRow = entityTopWorldY / gamePanel.getTileSize();
        int entityMiddleRow = (entityTopWorldY + entityBottomWorldY) / 2 / gamePanel.getTileSize();
        int entityBottomRow = entityBottomWorldY / gamePanel.getTileSize();

        // Character has collider 2x2 tiles
        int[] collidedTiles = new int[3];

        switch (entity.getDirection()) {
            case "up" -> {
                entityTopRow = (entityTopWorldY - entity.getSpeed()) / gamePanel.getTileSize();

                collidedTiles[0] = gamePanel.getTileManager().getMapTileNumbers()[entityTopRow][entityLeftCol];
                collidedTiles[1] = gamePanel.getTileManager().getMapTileNumbers()[entityTopRow][entityMiddleCol];
                collidedTiles[2] = gamePanel.getTileManager().getMapTileNumbers()[entityTopRow][entityRightCol];

                if (gamePanel.getTileManager().getTiles()[collidedTiles[0]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[1]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[2]].isCollision())
                {
                    entity.setCollisionOn(true);
                }
            }
            case "down" -> {
                entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / gamePanel.getTileSize();

                collidedTiles[0] = gamePanel.getTileManager().getMapTileNumbers()[entityBottomRow][entityLeftCol];
                collidedTiles[1] = gamePanel.getTileManager().getMapTileNumbers()[entityBottomRow][entityMiddleCol];
                collidedTiles[2] = gamePanel.getTileManager().getMapTileNumbers()[entityBottomRow][entityRightCol];

                if (gamePanel.getTileManager().getTiles()[collidedTiles[0]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[1]].isCollision()
                || gamePanel.getTileManager().getTiles()[collidedTiles[2]].isCollision())
                {
                    entity.setCollisionOn(true);
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / gamePanel.getTileSize();

                collidedTiles[0] = gamePanel.getTileManager().getMapTileNumbers()[entityTopRow][entityLeftCol];
                collidedTiles[1] = gamePanel.getTileManager().getMapTileNumbers()[entityMiddleRow][entityLeftCol];
                collidedTiles[2] = gamePanel.getTileManager().getMapTileNumbers()[entityBottomRow][entityLeftCol];

                if (gamePanel.getTileManager().getTiles()[collidedTiles[0]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[1]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[2]].isCollision())
                {
                    entity.setCollisionOn(true);
                }
            }
            case "right" -> {
                entityRightCol = (entityRightWorldX + entity.getSpeed()) / gamePanel.getTileSize();

                collidedTiles[0] = gamePanel.getTileManager().getMapTileNumbers()[entityTopRow][entityRightCol];
                collidedTiles[1] = gamePanel.getTileManager().getMapTileNumbers()[entityMiddleRow][entityRightCol];
                collidedTiles[2] = gamePanel.getTileManager().getMapTileNumbers()[entityBottomRow][entityRightCol];

                if (gamePanel.getTileManager().getTiles()[collidedTiles[0]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[1]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[2]].isCollision())
                {
                    entity.setCollisionOn(true);
                }
            }
        }
    }

    public int checkObject(Entity entity, boolean isPlayer) {
        int index = -1;

        for (int i = 0; i < gamePanel.obj.length; i++) {
            if (gamePanel.obj[i] != null) {
                // Extragem suprafata de coliziune
                entity.collisionArea.x = entity.getWorldX() + entity.collisionArea.x;
                entity.collisionArea.y = entity.getWorldY() + entity.collisionArea.y;

                gamePanel.obj[i].solidArea.x = gamePanel.obj[i].worldX + gamePanel.obj[i].solidArea.x;
                gamePanel.obj[i].solidArea.y = gamePanel.obj[i].worldY + gamePanel.obj[i].solidArea.y;

                switch (entity.getDirection()) {
                    case "up" -> {
                        entity.collisionArea.y -= entity.getSpeed();
                        if (entity.collisionArea.intersects(gamePanel.obj[i].solidArea)) {
                            if (gamePanel.obj[i].collision) {
                                entity.setCollisionOn(true);
                            }
                            if (isPlayer == true) {
                                index = i;
                            }
                        }
                    }
                    case "down" -> {
                        entity.collisionArea.y += entity.getSpeed();
                        if (entity.collisionArea.intersects(gamePanel.obj[i].solidArea)) {
                            if (gamePanel.obj[i].collision) {
                                entity.setCollisionOn(true);
                            }
                            if (isPlayer == true) {
                                index = i;
                            }
                        }
                    }
                    case "left" -> {
                        entity.collisionArea.x -= entity.getSpeed();
                        if (entity.collisionArea.intersects(gamePanel.obj[i].solidArea)) {
                            if (gamePanel.obj[i].collision) {
                                entity.setCollisionOn(true);
                            }
                            if (isPlayer == true) {
                                index = i;
                            }
                        }
                    }
                    case "right" -> {
                        entity.collisionArea.x += entity.getSpeed();
                        if (entity.collisionArea.intersects(gamePanel.obj[i].solidArea)) {
                            if (gamePanel.obj[i].collision) {
                                entity.setCollisionOn(true);
                            }
                            if (isPlayer == true) {
                                index = i;
                            }
                        }
                    }
                }
                entity.collisionArea.x = entity.solidAreaDefaultX;
                entity.collisionArea.y = entity.solidAreaDefaultY;
                gamePanel.obj[i].solidArea.x = gamePanel.obj[i].solidAreaDefaultX;
                gamePanel.obj[i].solidArea.y = gamePanel.obj[i].solidAreaDefaultY;
            }
        }
        return index;
    }
}
