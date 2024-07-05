package Characters;

import Boards.GameBoard;

import javax.swing.*;
import java.awt.*;

public abstract class Creature extends JPanel implements Runnable, Motion {
    protected int x, y;
    protected int startX, startY;
    protected int currentDirection;
    protected boolean running;
    protected int animationDelay;
    protected ImageIcon[][] motionImages;
    protected int currentFrame;
    protected int creatureSize;
    protected JLabel characterLabel;
    protected int speed;
    protected GameBoard gameBoard;

    public Creature(int tileSize, int startX, int startY, GameBoard gameBoard) {
        this.x = startX;
        this.y = startY;
        this.startX = startX;
        this.startY = startY;
        this.gameBoard = gameBoard;
        this.creatureSize = tileSize;
        this.speed = tileSize / 2;
        this.running = false;
        this.animationDelay = 50;
        this.currentDirection = 0;

        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    }

    protected abstract void loadImages();

    @Override
    public void run() {}

    @Override
    public void move() {}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}