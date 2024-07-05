package Characters;

import Boards.GameBoard;
import Game.GameThread;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class PacMan extends Creature implements KeyListener {
    private ImageIcon[] destructionImages;
    private int currentDirection = 0; //Default to upper direction
    private int desiredDirection = -1; //Default to no direction
    private boolean isOffScreen = false;
    private boolean destroying = false;

    public PacMan(int tileSize, int startX, int startY, GameBoard gameBoard) {
        super(tileSize, startX, startY, gameBoard);

        loadImages();

        characterLabel = new JLabel();
        characterLabel.setBounds(0, 0, creatureSize, creatureSize);
        characterLabel.setIcon(motionImages[0][0]);
        setOpaque(false);

        add(characterLabel);

        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();
    }

    @Override
    protected void loadImages() {
        motionImages = new ImageIcon[4][3];
        destructionImages = new ImageIcon[10];

        try {
            String path = "src/Textures/PacMan/";
            String[][] imagesPaths = new String[4][3];

            imagesPaths[0][0] = path + "UP/FullPacMan.png";
            imagesPaths[0][1] = path + "UP/HalfOpenMouth.png";
            imagesPaths[0][2] = path + "UP/FullyOpenMouth.png";

            imagesPaths[1][0] = path + "RIGHT/FullPacMan.png";
            imagesPaths[1][1] = path + "RIGHT/HalfOpenMouth.png";
            imagesPaths[1][2] = path + "RIGHT/FullyOpenMouth.png";

            imagesPaths[2][0] = path + "DOWN/FullPacMan.png";
            imagesPaths[2][1] = path + "DOWN/HalfOpenMouth.png";
            imagesPaths[2][2] = path + "DOWN/FullyOpenMouth.png";

            imagesPaths[3][0] = path + "LEFT/FullPacMan.png";
            imagesPaths[3][1] = path + "LEFT/HalfOpenMouth.png";
            imagesPaths[3][2] = path + "LEFT/FullyOpenMouth.png";

            for (int i = 0; i < motionImages.length; i++) {
                for (int j = 0; j < motionImages[i].length; j++) {
                    BufferedImage img = ImageIO.read(new File(imagesPaths[i][j]));
                    Image scaledImg = img.getScaledInstance(this.creatureSize, this.creatureSize, Image.SCALE_SMOOTH);

                    motionImages[i][j] = new ImageIcon(scaledImg);
                }
            }

            for (int i = 0; i < 10; i++) {
                String destructionImagesPaths = path + "Destruction/" + (i + 1) + ".png";

                BufferedImage img = ImageIO.read(new File(destructionImagesPaths));
                Image scaledImg = img.getScaledInstance(this.creatureSize, this.creatureSize, Image.SCALE_SMOOTH);

                destructionImages[i] = new ImageIcon(scaledImg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void move() {
        characterLabel.setIcon(null);

        if (desiredDirection != -1 && canMoveInDirection(desiredDirection)) {
            currentDirection = desiredDirection;
            desiredDirection = -1;
        }

        if (!tryMove(currentDirection)) {
            return;
        }

        characterLabel.setIcon(motionImages[currentDirection][currentFrame]);
        setBounds(x, y, creatureSize, creatureSize);
    }

    private boolean tryMove(int direction) {
        int nextX = x;
        int nextY = y;

        switch (direction) {
            case 0: nextY -= speed; break;
            case 1: nextX += speed; break;
            case 2: nextY += speed; break;
            case 3: nextX -= speed; break;
        }

        if (nextX < -creatureSize) {
            isOffScreen = true;
            nextX = gameBoard.getColumns() * creatureSize;
        } else if (nextX >= gameBoard.getColumns() * creatureSize) {
            isOffScreen = true;
            nextX = -creatureSize;
        }

        if (isOffScreen) {
            x = nextX;
            y = nextY;

            isOffScreen = false;
            return true;
        }

        if (canMoveToTile(nextX, nextY)) {
            x = nextX;
            y = nextY;

            int tileX = x / creatureSize;
            int tileY = y / creatureSize;

            if (x % creatureSize == 0 && y % creatureSize == 0) {
                int currentTile = gameBoard.getTile(tileX, tileY);

                if (currentTile == 1) {
                    gameBoard.setTile(tileX, tileY, 0);
                    GameThread.increaseScore(100);
                    GameThread.getHUD().updateScore(GameThread.getScore());
                } else if (currentTile == 2) {
                    gameBoard.setTile(tileX, tileY, 0);
                    GameThread.increaseScore(1000);
                    GameThread.getHUD().updateScore(GameThread.getScore());
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean canMoveToTile(int nextX, int nextY) {
        int topY = nextY;
        int rightX = nextX + creatureSize - 1;
        int bottomY = nextY + creatureSize - 1;
        int leftX = nextX;

        int leftTileX = (leftX < 0) ? gameBoard.getColumns() - 1 : leftX / creatureSize;
        int rightTileX = (rightX >= gameBoard.getColumns() * creatureSize) ? 0 : rightX / creatureSize;
        int topTileY = topY / creatureSize;
        int bottomTileY = bottomY / creatureSize;

        return isTileNotWall(leftTileX, topTileY) &&
                isTileNotWall(rightTileX, topTileY) &&
                isTileNotWall(leftTileX, bottomTileY) &&
                isTileNotWall(rightTileX, bottomTileY);
    }

    private boolean isTileNotWall(int tileX, int tileY) {
        if (tileX < 0 || tileX >= gameBoard.getColumns() || tileY < 0 || tileY >= gameBoard.getRows()) {
            return false;
        }

        int tile = gameBoard.getTile(tileX, tileY);
        return tile != 3 && tile != 4;
    }

    private boolean canMoveInDirection(int direction) {
        int testDX = 0;
        int testDY = 0;

        switch (direction) {
            case 0: testDY = -creatureSize / 2; break;
            case 1: testDX = creatureSize / 2; break;
            case 2: testDY = creatureSize / 2; break;
            case 3: testDX = -creatureSize / 2; break;
        }

        return canMoveToTile(x + testDX, y + testDY);
    }

    private synchronized void updateAnimationFrame(int direction) {
        currentFrame = (currentFrame + 1) % motionImages[direction].length;
        characterLabel.setIcon(motionImages[direction][currentFrame]);
    }

    private boolean creaturesCollide() {
        for (Creature ghost : GameThread.getGhosts()) {
            if (isTouching(this, ghost)) {
                return true;
            }
        }

        return false;
    }

    private boolean isTouching(JPanel j1, JPanel j2) {
        int c1Left = j1.getX();
        int c1Right = j1.getX() + j1.getWidth();
        int c1Top = j1.getY();
        int c1Bottom = j1.getY() + j1.getHeight();

        int c2Left = j2.getX();
        int c2Right = j2.getX() + j2.getWidth();
        int c2Top = j2.getY();
        int c2Bottom = j2.getY() + j2.getHeight();

        boolean xOverlap = (c1Left < c2Right) && (c1Right > c2Left);
        boolean yOverlap = (c1Top < c2Bottom) && (c1Bottom > c2Top);

        return xOverlap && yOverlap;
    }

    public void playDestructionAnimation(Runnable callback) {
        destroying = true;

        new Thread(() -> {
            for (ImageIcon destructionImage : destructionImages) {
                characterLabel.setIcon(destructionImage);
                try {
                    Thread.sleep(animationDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            callback.run();
        }).start();
    }

    public void setDestroying(boolean destroying) {
        this.destroying = destroying;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(destroying){
            return;
        }

        int key = e.getKeyCode();

        if (!running) {
            running = true;
            new Thread(this).start();
        }

        if (key == KeyEvent.VK_LEFT) {
            desiredDirection = 3;
        } else if (key == KeyEvent.VK_RIGHT) {
            desiredDirection = 1;
        } else if (key == KeyEvent.VK_UP) {
            desiredDirection = 0;
        } else if (key == KeyEvent.VK_DOWN) {
            desiredDirection = 2;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Not used, because Pac-Man moves by pressing key, not holding it
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Not used, because it's not needed.
    }

    @Override
    public synchronized void run() {
        while (running) {
            try{
                move();

                updateAnimationFrame(currentDirection);

                if(creaturesCollide()){
                    removeKeyListener(this);

                    this.setRunning(false);

                    for (Creature ghost : GameThread.getGhosts()){
                        ghost.setRunning(false);
                    }

                    GameThread.setPacManDied(true);
                }

                Thread.sleep(animationDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}