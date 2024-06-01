package com.vengeance.game.main;
import com.vengeance.game.entity.Entity;
import com.vengeance.game.entity.MobileEntity;
import com.vengeance.game.entity.Player;

public class CollisionChecker {

    private final GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    private void move(Entity entity) {
        switch (entity.getDirection()) {
            case WALK_UP -> entity.collisionArea.y -= entity.getSpeed();
            case WALK_DOWN -> entity.collisionArea.y += entity.getSpeed();
            case WALK_LEFT -> entity.collisionArea.x -= entity.getSpeed();
            case WALK_RIGHT -> entity.collisionArea.x += entity.getSpeed();
        }
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
            case WALK_UP -> {
                entityTopRow = (entityTopWorldY - entity.getSpeed()) / gamePanel.getTileSize();

                collidedTiles[0] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityTopRow][entityLeftCol];
                collidedTiles[1] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityTopRow][entityMiddleCol];
                collidedTiles[2] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityTopRow][entityRightCol];

                if (gamePanel.getTileManager().getTiles()[collidedTiles[0]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[1]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[2]].isCollision())
                {
                    entity.setCollisionOn(true);
                }
            }
            case WALK_DOWN -> {
                entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / gamePanel.getTileSize();

                collidedTiles[0] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityBottomRow][entityLeftCol];
                collidedTiles[1] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityBottomRow][entityMiddleCol];
                collidedTiles[2] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityBottomRow][entityRightCol];

                if (gamePanel.getTileManager().getTiles()[collidedTiles[0]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[1]].isCollision()
                || gamePanel.getTileManager().getTiles()[collidedTiles[2]].isCollision())
                {
                    entity.setCollisionOn(true);
                }
            }
            case WALK_LEFT -> {
                entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / gamePanel.getTileSize();

                collidedTiles[0] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityTopRow][entityLeftCol];
                collidedTiles[1] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityMiddleRow][entityLeftCol];
                collidedTiles[2] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityBottomRow][entityLeftCol];

                if (gamePanel.getTileManager().getTiles()[collidedTiles[0]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[1]].isCollision()
                        || gamePanel.getTileManager().getTiles()[collidedTiles[2]].isCollision())
                {
                    entity.setCollisionOn(true);
                }
            }
            case WALK_RIGHT -> {
                entityRightCol = (entityRightWorldX + entity.getSpeed()) / gamePanel.getTileSize();

                collidedTiles[0] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityTopRow][entityRightCol];
                collidedTiles[1] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityMiddleRow][entityRightCol];
                collidedTiles[2] = gamePanel.getTileManager().getCurrentMapTileNumbers()[entityBottomRow][entityRightCol];

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

        for (int i = 0; i < gamePanel.obj[gamePanel.currentMap].length; i++) {
            if (gamePanel.obj[gamePanel.currentMap][i] != null) {
                // Extragem suprafata de coliziune
                entity.collisionArea.x = entity.getWorldX() + entity.collisionArea.x;
                entity.collisionArea.y = entity.getWorldY() + entity.collisionArea.y;

                gamePanel.obj[gamePanel.currentMap][i].collisionArea.x = gamePanel.obj[gamePanel.currentMap][i].worldX + gamePanel.obj[gamePanel.currentMap][i].collisionArea.x;
                gamePanel.obj[gamePanel.currentMap][i].collisionArea.y = gamePanel.obj[gamePanel.currentMap][i].worldY + gamePanel.obj[gamePanel.currentMap][i].collisionArea.y;

                move(entity);

                if (entity.collisionArea.intersects(gamePanel.obj[gamePanel.currentMap][i].collisionArea)) {
                    if (gamePanel.obj[gamePanel.currentMap][i].collision) {
                        entity.setCollisionOn(true);
                    }
                    if (isPlayer) {
                        index = i;
                    }
                }

                entity.collisionArea.x = entity.solidAreaDefaultX;
                entity.collisionArea.y = entity.solidAreaDefaultY;
                gamePanel.obj[gamePanel.currentMap][i].collisionArea.x = gamePanel.obj[gamePanel.currentMap][i].solidAreaDefaultX;
                gamePanel.obj[gamePanel.currentMap][i].collisionArea.y = gamePanel.obj[gamePanel.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    // NPC or Monster collision
    public int checkEntity(MobileEntity entity, MobileEntity[][] target) {
        int index = -1;

        for (int i = 0; i < target[gamePanel.currentMap].length; i++) {
            if (target[gamePanel.currentMap][i] != null) {
                // Extragem suprafata de coliziune
                entity.collisionArea.x = entity.getWorldX() + entity.collisionArea.x;
                entity.collisionArea.y = entity.getWorldY() + entity.collisionArea.y;

                target[gamePanel.currentMap][i].collisionArea.x = target[gamePanel.currentMap][i].worldX + target[gamePanel.currentMap][i].collisionArea.x;
                target[gamePanel.currentMap][i].collisionArea.y = target[gamePanel.currentMap][i].worldY + target[gamePanel.currentMap][i].collisionArea.y;

                move(entity);
                if (entity.collisionArea.intersects(target[gamePanel.currentMap][i].collisionArea) && target[gamePanel.currentMap][i] != entity) {
                    entity.setCollisionOn(true);
                    index = i;
                }
                entity.collisionArea.x = entity.solidAreaDefaultX;
                entity.collisionArea.y = entity.solidAreaDefaultY;
                target[gamePanel.currentMap][i].collisionArea.x = target[gamePanel.currentMap][i].solidAreaDefaultX;
                target[gamePanel.currentMap][i].collisionArea.y = target[gamePanel.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public int checkEntity(Player entity, MobileEntity[][] target, boolean attacking) {
        int currentWorldX = entity.getWorldX();
        int currentWorldY = entity.getWorldY();
        int solidAreaWidth = entity.collisionArea.width;
        int solidAreaHeight = entity.collisionArea.height;

        entity.worldX -= entity.collisionArea.x - entity.attackArea.x;
        entity.worldY -= entity.collisionArea.x - entity.attackArea.y;
        entity.collisionArea.width = entity.attackArea.width;
        entity.collisionArea.height = entity.attackArea.height;

        int i = checkEntity(entity, target);

        entity.collisionArea.width = solidAreaWidth;
        entity.collisionArea.height = solidAreaHeight;
        entity.worldX = currentWorldX;
        entity.worldY = currentWorldY;

        return i;
    }

    public void checkPlayer(Entity entity) {
        // Extragem suprafata de coliziune
        entity.collisionArea.x = entity.getWorldX() + entity.collisionArea.x;
        entity.collisionArea.y = entity.getWorldY() + entity.collisionArea.y;

        gamePanel.player.collisionArea.x = gamePanel.player.worldX + gamePanel.player.collisionArea.x;
        gamePanel.player.collisionArea.y = gamePanel.player.worldY + gamePanel.player.collisionArea.y;

        switch (entity.getDirection()) {
            case WALK_UP -> {
                entity.collisionArea.y -= entity.getSpeed();
                if (entity.collisionArea.intersects(gamePanel.player.collisionArea)) {
                    entity.setCollisionOn(true);
                }
            }
            case WALK_DOWN -> {
                entity.collisionArea.y += entity.getSpeed();
                if (entity.collisionArea.intersects(gamePanel.player.collisionArea)) {
                    entity.setCollisionOn(true);
                }
            }
            case WALK_LEFT -> {
                entity.collisionArea.x -= entity.getSpeed();
                if (entity.collisionArea.intersects(gamePanel.player.collisionArea)) {
                    entity.setCollisionOn(true);
                }
            }
            case WALK_RIGHT -> {
                entity.collisionArea.x += entity.getSpeed();
                if (entity.collisionArea.intersects(gamePanel.player.collisionArea)) {
                    entity.setCollisionOn(true);
                }
            }
        }
        entity.collisionArea.x = entity.solidAreaDefaultX;
        entity.collisionArea.y = entity.solidAreaDefaultY;
        gamePanel.player.collisionArea.x = gamePanel.player.solidAreaDefaultX;
        gamePanel.player.collisionArea.y = gamePanel.player.solidAreaDefaultY;
    }
}
