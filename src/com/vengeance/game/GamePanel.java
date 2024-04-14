package com.vengeance.game;

import com.vengeance.game.entity.*;
import com.vengeance.game.tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

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

    // FPS
    private final int FPS = 60;

    private final TileManager tileManager = new TileManager(this);
    private final KeyHandler keyHandler = new KeyHandler();

    private Thread gameThread;

    private final CollisionChecker collisionChecker = new CollisionChecker(this);

    private final Player player = new Player(this, keyHandler);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(0,0,0));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
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
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();

                repaint();

                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;

        tileManager.draw(graphics2D);
        player.draw(graphics2D);

        graphics2D.dispose();
    }

    public int getScale() { return scale; }

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxScreenColumns() {
        return maxScreenColumns;
    }

    public int getMaxScreenRows() {
        return maxScreenRows;
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

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
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
}
