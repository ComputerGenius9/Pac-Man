package Game;

import Boards.*;
import Characters.Creature;
import Characters.Ghosts.Blinky;
import Characters.Ghosts.Clyde;
import Characters.Ghosts.Inky;
import Characters.Ghosts.Pinky;
import Characters.PacMan;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameThread extends Thread {

    //Game states
    private boolean run;
    private static boolean pacManDied;
    private boolean resetting;

    private static GameBoard currentGameBoard;
    private int lives;
    private static int score;
    private final int TILE_SIZE;
    private PacMan pacMan;
    private Blinky blinky;
    private Inky inky;
    private Clyde clyde;
    private Pinky pinky;
    private static List<Creature> ghosts;
    private static HUD hud;
    private boolean gameStarted;
    private final int[][] currentBoard;
    private final int[][] previousBoard;

    public GameThread(GameBoard gameBoard) {
        currentGameBoard = gameBoard;
        this.lives = 3;
        score = 0;
        this.run = false;
        pacManDied = false;
        this.resetting = false;
        this.TILE_SIZE = gameBoard.getTileSize();
        this.currentBoard = gameBoard.getBoard();
        this.previousBoard = new int[currentBoard.length][currentBoard[0].length];

        for (int i = 0; i < currentBoard.length; i++) {
            System.arraycopy(currentBoard[i], 0, previousBoard[i], 0, currentBoard[i].length);
        }

        setCreatures();

        hud = new HUD(gameBoard);
        gameBoard.getGameLayeredPane().add(hud, 3);
        hud.setBounds(0, 0, gameBoard.getColumns() * TILE_SIZE, gameBoard.getRows() * TILE_SIZE);

        gameBoard.setVisible(true);

        this.run = true;
    }

    private void setCreatures() {
        setPacMan();
        setBlinky();
        setClyde();
        setInky();
        setPinky();

        ghosts = new ArrayList<>();

        ghosts.add(pinky);
        ghosts.add(inky);
        ghosts.add(blinky);
        ghosts.add(clyde);

        for (Creature creature : ghosts) {
            currentGameBoard.addCreature(creature);
        }

        currentGameBoard.addCreature(pacMan);
    }

    public void setPacMan() {
        int startX = 0;
        int startY = 0;

        switch (currentGameBoard.getTypeOfBoard()) {
            case "SmallGB":
                startX = 7 * TILE_SIZE;
                startY = 10 * TILE_SIZE;
                break;
            case "MediumGB":
                startX = 9 * TILE_SIZE;
                startY = 22 * TILE_SIZE;
                break;
            case "LargeGB":
                startX = 13 * TILE_SIZE;
                startY = 26 * TILE_SIZE;
                break;
            case "ExtraLargeGB":
                startX = 20 * TILE_SIZE;
                startY = 22 * TILE_SIZE;
                break;
            case "GiganticGB":
                startX = 24 * TILE_SIZE;
                startY = 25 * TILE_SIZE;
                break;
            default:
                break;
        }

        pacMan = new PacMan(TILE_SIZE, startX, startY, currentGameBoard);
    }

    private void setBlinky() {
        int startX = 0;
        int startY = 0;

        switch (currentGameBoard.getTypeOfBoard()) {
            case "SmallGB":
                startX = TILE_SIZE;
                startY = 4 * TILE_SIZE;
                break;
            case "MediumGB":
                startX = 12 * TILE_SIZE;
                startY = 10 * TILE_SIZE;
                break;
            case "LargeGB":
                startX = 13 * TILE_SIZE;
                startY = 14 * TILE_SIZE;
                break;
            case "ExtraLargeGB":
                startX = 23 * TILE_SIZE;
                startY = 18 * TILE_SIZE;
                break;
            case "GiganticGB":
                startX = 42 * TILE_SIZE;
                startY = 32 * TILE_SIZE;
                break;
            default:
                break;
        }

        blinky = new Blinky(TILE_SIZE, startX, startY, currentGameBoard);
    }

    private void setInky() {
        int startX = 0;
        int startY = 0;

        switch (currentGameBoard.getTypeOfBoard()) {
            case "SmallGB":
                startX = 13 * TILE_SIZE;
                startY = 4 * TILE_SIZE;
                break;
            case "MediumGB":
                startX = 7 * TILE_SIZE;
                startY = 10 * TILE_SIZE;
                break;
            case "LargeGB":
                startX = 18 * TILE_SIZE;
                startY = 17 * TILE_SIZE;
                break;
            case "ExtraLargeGB":
                startX = 16 * TILE_SIZE;
                startY = 18 * TILE_SIZE;
                break;
            case "GiganticGB":
                startX = 7 * TILE_SIZE;
                startY = 32 * TILE_SIZE;
                break;
            default:
                break;
        }

        inky = new Inky(TILE_SIZE, startX, startY, currentGameBoard);
    }

    private void setClyde() {
        int startX = 0;
        int startY = 0;

        switch (currentGameBoard.getTypeOfBoard()) {
            case "SmallGB":
                startX = TILE_SIZE;
                startY = 16 * TILE_SIZE;
                break;
            case "MediumGB":
                startX = 7 * TILE_SIZE;
                startY = 15 * TILE_SIZE;
                break;
            case "LargeGB":
                startX = 13 * TILE_SIZE;
                startY = 20 * TILE_SIZE;
                break;
            case "ExtraLargeGB":
                startX = 23 * TILE_SIZE;
                startY = 13 * TILE_SIZE;
                break;
            case "GiganticGB":
                startX = 42 * TILE_SIZE;
                startY = 18 * TILE_SIZE;
                break;
            default:
                break;
        }

        clyde = new Clyde(TILE_SIZE, startX, startY, currentGameBoard);
    }

    private void setPinky() {
        int startX = 0;
        int startY = 0;

        switch (currentGameBoard.getTypeOfBoard()) {
            case "SmallGB":
                startX = 13 * TILE_SIZE;
                startY = 16 * TILE_SIZE;
                break;
            case "MediumGB":
                startX = 12 * TILE_SIZE;
                startY = 15 * TILE_SIZE;
                break;
            case "LargeGB":
                startX = 9 * TILE_SIZE;
                startY = 17 * TILE_SIZE;
                break;
            case "ExtraLargeGB":
                startX = 16 * TILE_SIZE;
                startY = 13 * TILE_SIZE;
                break;
            case "GiganticGB":
                startX = 7 * TILE_SIZE;
                startY = 18 * TILE_SIZE;
                break;
            default:
                break;
        }

        pinky = new Pinky(TILE_SIZE, startX, startY, currentGameBoard);
    }

    private boolean checkPacManLives() {
        return lives == 0;
    }

    private void startGhosts() {
        for (Creature ghost : ghosts) {
            ghost.setRunning(true);
        }
    }

    private void resetGame() {
        if(resetting){
            return;
        }

        resetting = true;

        hud.updateLives(--lives);

        pacMan.playDestructionAnimation(() -> {
            if (lives == 0) {
                String playerName = JOptionPane.showInputDialog(currentGameBoard, "Game Over! Enter your name:", "Game Over", JOptionPane.PLAIN_MESSAGE);

                HighScores highScore = new HighScores(playerName, score, currentGameBoard.getTypeOfBoard());

                List<HighScores> highScores = HighScores.loadHighScores();

                highScores.add(highScore);

                HighScores.saveHighScores(highScores);

                run = false;

                SwingUtilities.invokeLater(() -> new PacManGame().setVisible(true));

                currentGameBoard.dispose();
            } else {
                for (Creature ghost : ghosts) {
                    currentGameBoard.removeCreature(ghost);
                }

                ghosts.clear();

                gameStarted = false;
                pacManDied = false;

                currentGameBoard.removeCreature(pacMan);

                setCreatures();

                SwingUtilities.invokeLater(() -> currentGameBoard.addKeyListener(pacMan));

                pacMan.setFocusable(true);
                pacMan.requestFocusInWindow();

                resetting = false;
                pacMan.setDestroying(false);
            }
        });
    }

    private synchronized void continueGame(){
        if (resetting){
            return;
        }

        resetting = true;

        pacMan.setRunning(false);

        for (Creature ghost : GameThread.getGhosts()){
            ghost.setRunning(false);
        }

        for (Creature ghost : ghosts) {
            currentGameBoard.removeCreature(ghost);
        }

        ghosts.clear();

        gameStarted = false;
        pacManDied = false;

        currentGameBoard.removeCreature(pacMan);

        for (int i = 0; i < previousBoard.length; i++) {
            System.arraycopy(previousBoard[i], 0, currentBoard[i], 0, previousBoard[i].length);
        }

        currentGameBoard.setBoard(currentBoard);

        setCreatures();
        SwingUtilities.invokeLater(() -> currentGameBoard.addKeyListener(pacMan));

        pacMan.setFocusable(true);
        pacMan.requestFocusInWindow();

        resetting = false;
    }

    public boolean pacManWon(){
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                if (currentBoard[i][j] == 1 || currentBoard [i][j] == 2){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public synchronized void run() {
        while (run) {
            try{
                if (pacMan.isRunning() && !gameStarted) {
                    startGhosts();
                    gameStarted = true;
                }

                if (checkPacManLives()) {
                    run = false;
                }

                if (pacManDied && !resetting){
                    resetGame();
                }

                if (pacManWon() && !resetting){
                    continueGame();
                }

                hud.updateScore(score);
                hud.updateLives(lives);

                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Creature> getGhosts(){
        return ghosts;
    }

    public static synchronized void increaseScore(int points) {
        score += points;
    }

    public static int getScore() {
        return score;
    }

    public static HUD getHUD() {
        return hud;
    }

    public static void setPacManDied(boolean pacManDied) {
        GameThread.pacManDied = pacManDied;
    }
}