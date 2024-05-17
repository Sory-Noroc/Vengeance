package com.vengeance.game.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.lang.System.exit;

public class KeyHandler implements KeyListener {

    public boolean enterPressed, spacePressed;
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private final GamePanel gamePanel;

    public KeyHandler(GamePanel gp) {
        gamePanel = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gamePanel.gameState == GamePanel.GAME_STATE.MENU_STATE) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gamePanel.ui.commandNum--;
                if (gamePanel.ui.commandNum < 0) {
                    gamePanel.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gamePanel.ui.commandNum++;
                if (gamePanel.ui.commandNum > 2) {
                    gamePanel.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gamePanel.ui.commandNum == 0) {
                    gamePanel.gameState = GamePanel.GAME_STATE.PLAY_STATE;
                    gamePanel.playMusic(0);
                } else if (gamePanel.ui.commandNum == 1) {
                    // Database loading

                } else if (gamePanel.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        } else if (gamePanel.gameState == GamePanel.GAME_STATE.PLAY_STATE) {
            if (code == KeyEvent.VK_W) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
            if (code == KeyEvent.VK_SPACE) {
                spacePressed = true;
            }

        } else if (gamePanel.gameState == GamePanel.GAME_STATE.DIALOG_STATE) {
            if (code == KeyEvent.VK_ENTER) {
                gamePanel.gameState = GamePanel.GAME_STATE.PLAY_STATE;
            }
        } else if (gamePanel.gameState == GamePanel.GAME_STATE.GAME_OVER_STATE) {
            gameOverState(code);
        }
    }

    public void gameOverState(int code) {
        if (code == KeyEvent.VK_W) {
            gamePanel.ui.commandNum--;
            if (gamePanel.ui.commandNum < 0) {
                gamePanel.ui.commandNum = 1;
            }
        } else if (code == KeyEvent.VK_S) {
            gamePanel.ui.commandNum++;
            if (gamePanel.ui.commandNum > 1) {
                gamePanel.ui.commandNum = 0;
            }
        } if (code == KeyEvent.VK_ENTER) {
            if (gamePanel.ui.commandNum == 0) {
                gamePanel.gameState = GamePanel.GAME_STATE.PLAY_STATE;
                gamePanel.player.setDefaultValues();
                gamePanel.setUpGame();
            } else if (gamePanel.ui.commandNum == 1) {
                gamePanel.gameState = GamePanel.GAME_STATE.MENU_STATE;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }

        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }

        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }

        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }

        if (code == KeyEvent.VK_P) {
            if (gamePanel.gameState == GamePanel.GAME_STATE.PAUSE_STATE) {
                gamePanel.gameState = GamePanel.GAME_STATE.PLAY_STATE;
            } else if (gamePanel.gameState == GamePanel.GAME_STATE.PLAY_STATE) {
                gamePanel.gameState = GamePanel.GAME_STATE.PAUSE_STATE;
            }
        }
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public KeyHandler setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
        return this;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public KeyHandler setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
        return this;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public KeyHandler setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
        return this;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public KeyHandler setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
        return this;
    }
}
