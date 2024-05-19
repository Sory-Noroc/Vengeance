package com.vengeance.game.events;

import com.vengeance.game.main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.vengeance.game.main.GamePanel.GAME_STATE.*;
import static java.lang.System.exit;

public class KeyHandler implements KeyListener {

    public boolean enterPressed, spacePressed, ePressed;
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

        if (code == KeyEvent.VK_K) {
            gamePanel.saveData();
            exit(0);
        }

        if (gamePanel.gameState == MENU_STATE) {
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
                    gamePanel.loadData();
                    gamePanel.gameState = GamePanel.GAME_STATE.PLAY_STATE;
                    gamePanel.playMusic(0);

                } else if (gamePanel.ui.commandNum == 2) {
                    gamePanel.saveData();
                    System.exit(0);
                }
            }
        } else if (gamePanel.gameState == GamePanel.GAME_STATE.PLAY_STATE) {
            // PLAY STATE

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
            if (code == KeyEvent.VK_E) {
                ePressed = true;
            }

        } else if (gamePanel.gameState == GamePanel.GAME_STATE.DIALOG_STATE) {
            if (code == KeyEvent.VK_ENTER) {
                gamePanel.gameState = GamePanel.GAME_STATE.PLAY_STATE;
            }
        } else if (gamePanel.gameState == GamePanel.GAME_STATE.GAME_OVER_STATE) {
            ePressed = false;
            gameOverState(code);

        } else if (gamePanel.gameState == WON_STATE) {
            gameWonState(code);
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
        } else if (code == KeyEvent.VK_ENTER) {
            if (gamePanel.ui.commandNum == 0) {
                gamePanel.gameState = GamePanel.GAME_STATE.PLAY_STATE;
                gamePanel.player.setDefaultValues();
                gamePanel.setUpGame();

            } else if (gamePanel.ui.commandNum == 1) {
                gamePanel.gameState = MENU_STATE;
            }
        }
    }

    public void gameWonState(int code) {
        gamePanel.npc[1][0].update();
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
        } else if (code == KeyEvent.VK_ENTER) {
            if (gamePanel.ui.commandNum == 0) {
                // Continue button
                gamePanel.gameState = PLAY_STATE;

            } else if (gamePanel.ui.commandNum == 1) {
                // Quit button
                gamePanel.saveData();
                exit(0);
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

        if (code == KeyEvent.VK_E) {
            ePressed = false;
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

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

}
