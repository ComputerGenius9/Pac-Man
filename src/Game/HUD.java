package Game;

import Boards.GameBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HUD extends JPanel {
    private JLabel scoreLabel;
    private JPanel livesPanel;
    private int score;
    private int lives;
    private ImageIcon heartIcon;
    private final GameBoard gameBoard;

    public HUD(GameBoard gameBoard) {
        this.score = 0;
        this.lives = 3;
        this.gameBoard = gameBoard;

        setOpaque(false);
        setLayout(null);

        loadHeartIcon();
        setUpHUD();
    }

    private void loadHeartIcon() {
        try {
            BufferedImage heartImage = ImageIO.read(new File("src/Textures/PacMan/Heart.png"));
            int scaledSize = gameBoard.getTileSize();
            Image scaledHeartImage = heartImage.getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
            heartIcon = new ImageIcon(scaledHeartImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpHUD() {
        JLabel highScoreLabel = new JLabel("HIGH SCORE");
        highScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setBounds((gameBoard.getColumns() * gameBoard.getTileSize()) / 2 - 50, 5, 150, 30);
        add(highScoreLabel);

        scoreLabel = new JLabel(String.valueOf(score));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds((gameBoard.getColumns() * gameBoard.getTileSize()) / 2 - 20, 35, 50, 30);
        add(scoreLabel);

        livesPanel = new JPanel();
        livesPanel.setOpaque(false);
        livesPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        livesPanel.setBounds(gameBoard.getColumns() * gameBoard.getTileSize() - 100, 5, 100, 30);
        add(livesPanel);

        updateLivesPanel();
    }

    private void updateLivesPanel() {
        livesPanel.removeAll();

        for (int i = 0; i < lives; i++) {
            JLabel heartLabel = new JLabel(heartIcon);
            livesPanel.add(heartLabel);
        }

        livesPanel.revalidate();
    }

    public void updateScore(int newScore) {
        SwingUtilities.invokeLater(() -> {
            this.score = newScore;
            scoreLabel.setText(String.valueOf(score));

            scoreLabel.revalidate();
        });
    }

    public void updateLives(int newLives) {
        this.lives = newLives;
        updateLivesPanel();
    }
}