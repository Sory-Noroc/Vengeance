package com.vengeance.game.main;

import com.vengeance.game.animation.AnimationFactory;
import com.vengeance.game.entity.*;
import com.vengeance.game.events.EventHandler;
import com.vengeance.game.events.KeyHandler;
import com.vengeance.game.object.SuperObject;
import com.vengeance.game.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {

    static GamePanel INSTANCE = null;

    public enum GAME_STATE { PLAY_STATE, PAUSE_STATE, MENU_STATE, DIALOG_STATE, GAME_OVER_STATE, WON_STATE }

    // SCREEN SETTINGS
    private final int originalTileSize = 8;
    private final int scale = 4;

    private final int tileSize = originalTileSize * scale;
    private final int maxScreenColumns = 36;
    private final int maxScreenRows = 24;
    private final int screenWidth = tileSize * maxScreenColumns; // 768 px
    private final int screenHeight = tileSize * maxScreenRows; // 576 px

    // WORLD SETTINGS
    private final int maxWorldColumns = 50;
    private final int maxWorldRows = 50;
    private final int worldWidth = tileSize * maxWorldColumns;
    private final int worldHeight = tileSize * maxWorldRows;
    public final int maxMap = 4;
    public int currentMap = 0;

    // FPS
    private final int FPS = 60;

    public final TileManager tileManager = new TileManager(this);
    public final KeyHandler keyHandler = new KeyHandler(this);
    private final AssetSetter assetSetter = new AssetSetter(this);

    private Thread gameThread;

    private final CollisionChecker collisionChecker = new CollisionChecker(this);

    public final Player player = new Player(this, keyHandler);
    public Sound music = new Sound();
    public Sound se = new Sound();
    public SuperObject[][] obj = new SuperObject[maxMap][10];
    public MobileEntity[][] npc = new MobileEntity[maxMap][10];
    public MobileEntity[][] enemy = new MobileEntity[maxMap][10];
    ArrayList<Entity> entityList = new ArrayList<>();
    public UI ui = new UI(this);
    public EventHandler eventHandler = new EventHandler(this);

    // Game State
    public GAME_STATE gameState = GAME_STATE.MENU_STATE;

    private GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(0,0,0));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public static GamePanel getInstance() {
        if (INSTANCE == null) {
            return new GamePanel();
        } else {
            return INSTANCE;
        }
    }

    public void setUpGame() {
        assetSetter.setObjects();
        assetSetter.setNPCs();
        assetSetter.setEnemies();
//        playMusic(0);
    }

    public void startGameThread() {
        this.gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        long timer = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();

                repaint();

                delta--;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == GAME_STATE.PLAY_STATE) {
            AnimationFactory.updateAnimations();
            player.update();
            // NPC update
            for (int i = 0; i < npc[currentMap].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            for (int i = 0; i < enemy[currentMap].length; i++) {
                if (enemy[currentMap][i] != null) {
                    enemy[currentMap][i].update();
                }
            }
        }
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        if (gameState != GAME_STATE.MENU_STATE) {
            tileManager.draw(graphics2D);

            entityList.add(player);
            for (int i = 0; i < npc[currentMap].length; i++) {
                if (npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }

            for (int i = 0; i < obj[currentMap].length; i++) {
                if (obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }

            for (int i = 0; i < enemy[currentMap].length; i++) {
                if (enemy[currentMap][i] != null) {
                    entityList.add(enemy[currentMap][i]);
                }
            }

            // Sorting in order of drawing from top to bottom
            entityList.sort(Comparator.comparingInt(Entity::getWorldY));

            // Draw entities
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(graphics2D);
            }
            entityList.clear();

            // Debugging player position
            int x = 10, y = 400;
            graphics2D.drawString("Col: " + (player.worldX + player.collisionArea.x)/getTileSize(), x, y);
            y += 30;
            graphics2D.drawString("Row: " + (player.worldY + player.collisionArea.y)/getTileSize(), x, y);
        }
        ui.draw(graphics2D);
        graphics2D.dispose();
    }

    public int getScale() { return scale; }

    public int getTileSize() {
        return tileSize;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getMaxWorldColumns() {
        return maxWorldColumns;
    }

    public int getMaxWorldRows() {
        return maxWorldRows;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    public Player getPlayer() {
        return player;
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}
