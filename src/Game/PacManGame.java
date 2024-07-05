package Game;

import Boards.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacManGame extends JFrame{
    private ArrayList<JButton> menuButtons;
    private ArrayList<JButton> mapSizeSelection;
    private final JPanel pan;
    private JTextArea instructionText;
    private static final int MAX_BUTTON_WIDTH = 400;

    public PacManGame(){
        setTitle("Pac-Man");
        setPreferredSize(new Dimension(600, 800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        pan.setBackground(Color.BLACK);

        showMainMenu();

        add(pan, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeButtons();
                if (instructionText != null)
                    resizeInstructionText();
            }
        });

        pan.setVisible(true);
    }
    private void showMainMenu(){
        pan.removeAll();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        JButton newGameButton = createStyledButton("New Game");
        JButton highScoresButton = createStyledButton("High scores");
        JButton exitButton = createStyledButton("Exit");

        menuButtons = new ArrayList<>();
        menuButtons.add(newGameButton);
        menuButtons.add(highScoresButton);
        menuButtons.add(exitButton);

        newGameButton.addActionListener(e -> showBoardsSelection());

        highScoresButton.addActionListener(e -> showHighScores());

        exitButton.addActionListener(e -> System.exit(0));

        pan.add(Box.createVerticalGlue());
        pan.add(newGameButton);
        pan.add(Box.createVerticalStrut(10));
        pan.add(highScoresButton);
        pan.add(Box.createVerticalStrut(10));
        pan.add(exitButton);
        pan.add(Box.createVerticalGlue());

        pan.repaint();

        resizeButtons();
    }
    private void showBoardsSelection() {
        pan.removeAll();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        mapSizeSelection = new ArrayList<>();

        String[] boardSizes = {"Small", "Medium", "Large", "Extra Large", "Gigantic"};

        instructionText = new JTextArea("Please select one of the following game boards:");
        instructionText.setFont(new Font("Courier New", Font.BOLD, 30));
        instructionText.setForeground(Color.WHITE);
        instructionText.setBackground(Color.BLACK);
        instructionText.setLineWrap(true);
        instructionText.setWrapStyleWord(true);
        instructionText.setEditable(false);
        instructionText.setFocusable(false);

        JPanel instructionPanel = new JPanel(new GridBagLayout());
        instructionPanel.setBackground(Color.BLACK);
        instructionPanel.setBorder(new EmptyBorder(0, 50, 10, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        instructionPanel.add(instructionText, gbc);

        JScrollPane instructionScrollPane = new JScrollPane(instructionPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        instructionScrollPane.setBorder(null);

        JButton returnButton = createImageButton(100, 100);
        returnButton.addActionListener(e -> showMainMenu());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.BLACK);
        topPanel.add(returnButton);

        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setBackground(Color.BLACK);

        centralPanel.add(Box.createVerticalGlue());
        centralPanel.add(instructionScrollPane);
        centralPanel.add(Box.createVerticalStrut(20));

        for(String size : boardSizes) {
            JButton sizeButton = createStyledButton(size);
            mapSizeSelection.add(sizeButton);

            sizeButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
                GameBoard gameBoard = switch (size) {
                    case "Small" -> new SmallGB();
                    case "Medium" -> new MediumGB();
                    case "Large" -> new LargeGB();
                    case "Extra Large" -> new ExtraLargeGB();
                    case "Gigantic" -> new GiganticGB();
                    default -> null;
                };

                if (gameBoard != null) {
                    GameThread gameThread = new GameThread(gameBoard);
                    gameThread.start();
                    dispose();
                }
            }));


            JPanel mapSizeSelectionPanel = new JPanel();
            mapSizeSelectionPanel.setLayout(new BoxLayout(mapSizeSelectionPanel, BoxLayout.X_AXIS));
            mapSizeSelectionPanel.setBackground(Color.BLACK);

            mapSizeSelectionPanel.add(Box.createHorizontalGlue());
            mapSizeSelectionPanel.add(sizeButton);
            mapSizeSelectionPanel.add(Box.createHorizontalGlue());

            centralPanel.add(mapSizeSelectionPanel);
            centralPanel.add(Box.createVerticalStrut(20));
        }

        centralPanel.add(Box.createVerticalGlue());

        pan.add(topPanel, BorderLayout.NORTH);
        pan.add(centralPanel, BorderLayout.CENTER);

        pan.repaint();

        resizeButtons();
    }
    private void showHighScores(){
        pan.removeAll();

        List<HighScores> highScores = HighScores.loadHighScores();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (HighScores highScore : highScores) {
            listModel.addElement(highScore.toString());
        }

        if (listModel.isEmpty()) {
            listModel.addElement("Player: Test Player, Score: 0, Board: TestBoard");
        }

        JList<String> highScoresList = new JList<>(listModel);

        highScoresList.setFont(new Font("Courier New", Font.BOLD, 18));
        highScoresList.setForeground(Color.WHITE);
        highScoresList.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(highScoresList);

        JButton returnButton = createImageButton(100, 100);
        returnButton.addActionListener(e -> showMainMenu());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.BLACK);
        topPanel.add(returnButton);

        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setBackground(Color.BLACK);

        centralPanel.add(Box.createVerticalGlue());
        centralPanel.add(scrollPane);
        centralPanel.add(Box.createVerticalGlue());

        pan.add(topPanel, BorderLayout.NORTH);
        pan.add(centralPanel, BorderLayout.CENTER);

        pan.repaint();

        revalidate();
    }

    private JButton createStyledButton(String text){
        Font retroFont = new Font("Courier New", Font.BOLD, 25);

        JButton styledButton = new JButton(text);

        styledButton.setFont(retroFont);
        styledButton.setForeground(Color.WHITE);
        styledButton.setBackground(Color.BLACK);
        styledButton.setFocusPainted(false);
        styledButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        return styledButton;
    }

    private void resizeButtons(){
        int frameWidth = getWidth();
        int frameHeight = getHeight();

        for (JButton button : menuButtons){
            int buttonWidth = Math.min(frameWidth / 2, MAX_BUTTON_WIDTH);
            int buttonHeight = frameHeight / 10;
            button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            button.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        }

        if(mapSizeSelection != null){
            for (JButton button : mapSizeSelection){
                int buttonWidth = Math.min(frameWidth / 2, MAX_BUTTON_WIDTH);
                int buttonHeight = frameHeight / 10;
                button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                button.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
            }
        }


        pan.revalidate();
    }

    private void resizeInstructionText(){
        int frameWidth = getWidth();
        instructionText.setPreferredSize(new Dimension(frameWidth - 100, instructionText.getPreferredSize().height));
        instructionText.revalidate();
    }

    private JButton createImageButton(int width, int height) {
        BufferedImage defaultImg = null;
        BufferedImage activeImg = null;

        try {
            defaultImg = ImageIO.read(new File("src/Textures/General/BackArrow.png"));
            activeImg = ImageIO.read(new File("src/Textures/General/ActiveBackArrow.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        if (defaultImg != null && activeImg != null) {
            Image scaledImg = defaultImg.getScaledInstance(width, height, Image.SCALE_FAST);
            ImageIcon defaultIcon = new ImageIcon(scaledImg);


            Image activeScaledImg = activeImg.getScaledInstance(width, height, Image.SCALE_FAST);
            ImageIcon activeIcon = new ImageIcon(activeScaledImg);


            JButton button = new JButton(defaultIcon);
            button.setPreferredSize(new Dimension(defaultIcon.getIconWidth(), defaultIcon.getIconHeight()));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);

            button.addMouseListener(new java.awt.event.MouseAdapter(){
                @Override
                public void mousePressed(java.awt.event.MouseEvent event){
                    button.setIcon(activeIcon);
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent event){
                    button.setIcon(defaultIcon);
                }

            });
            return button;
        }
        return new JButton("<-");
    }
}