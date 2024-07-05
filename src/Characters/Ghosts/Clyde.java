package Characters.Ghosts;

import Boards.GameBoard;
import Characters.Creature;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Clyde extends Creature {
    private Thread clydeThread;
    private final Random random;

    public Clyde(int tileSize, int startX, int startY, GameBoard gameBoard){
        super(tileSize, startX, startY, gameBoard);
        this.random = new Random();

        loadImages();

        characterLabel = new JLabel();
        characterLabel.setBounds(0, 0, creatureSize, creatureSize);
        characterLabel.setIcon(motionImages[0][0]);
        setOpaque(false);

        add(characterLabel);
    }

    @Override
    protected void loadImages() {
        String[] imageNames = {"UP1.png", "UP2.png", "RIGHT1.png", "RIGHT2.png", "DOWN1.png", "DOWN2.png", "LEFT1.png", "LEFT2.png"};
        motionImages = new ImageIcon[4][2];

        int imageIndex = 0;

        try {
            for (int i = 0; i < motionImages.length; ++i) {
                for (int j = 0; j < motionImages[i].length; ++j) {
                    String path = "src/Textures/Ghosts/Clyde/" + imageNames[imageIndex++];

                    BufferedImage img = ImageIO.read(new File(path));
                    Image scaledImg = img.getScaledInstance(creatureSize, creatureSize, Image.SCALE_SMOOTH);

                    motionImages[i][j] = new ImageIcon(scaledImg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void move() {
        int nextX = x;
        int nextY = y;

        switch (currentDirection) {
            case 0:
                nextY -= speed;
                break;
            case 1:
                nextX += speed;
                break;
            case 2:
                nextY += speed;
                break;
            case 3:
                nextX -= speed;
                break;
        }

        if (!canMoveToTile(nextX, nextY)) {
            changeDirection();
            return;
        }

        if (nextX < -creatureSize) {
            nextX = gameBoard.getColumns() * creatureSize;
        } else if (nextX >= gameBoard.getColumns() * creatureSize) {
            nextX = -creatureSize;
        }

        x = nextX;
        y = nextY;
        setLocation(x, y);
    }

    private void changeDirection() {
        int[] directions = {0, 1, 2, 3};

        int newDirection = directions[random.nextInt(4)];

        while (newDirection == currentDirection) {
            newDirection = directions[random.nextInt(4)];
        }

        currentDirection = newDirection;
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
        int tile = gameBoard.getTile(tileX, tileY);

        return tile != 3 && tile != 4;
    }

    @Override
    public synchronized void run() {
        while (running) {
            try{

            move();

            updateAnimationFrame(currentDirection);
                Thread.sleep(animationDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void updateAnimationFrame(int currentDirection){
        currentFrame = (currentFrame + 1) % motionImages[currentDirection].length;
        characterLabel.setIcon(motionImages[currentDirection][currentFrame]);
    }

    public void setRunning(boolean running) {
        this.running = running;

        if (running) {
            if (clydeThread == null || !clydeThread.isAlive()) {
                clydeThread = new Thread(this);
                clydeThread.start();
            }
        }
    }
}