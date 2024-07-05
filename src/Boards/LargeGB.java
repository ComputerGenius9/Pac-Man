package Boards;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LargeGB extends GameBoard {
    public LargeGB() {
        super();

        typeOfMap = "LargeGB";
        TILE_SIZE = 20;
        ROWS = 36;
        COLUMNS = 28;

        setPreferredSize(new Dimension(COLUMNS * TILE_SIZE + 12, ROWS * TILE_SIZE + 35));

        board = new int[ROWS][COLUMNS];

        setUpBoard();
    }

    @Override
    protected void setUpBoard(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/Boards/TXT/" + typeOfMap + ".txt"));

            boardPanel = new JPanel(new GridLayout(ROWS, COLUMNS));

            for (int i = 0; i < ROWS; ++i) {
                String line = br.readLine();

                if (line == null) {
                    break;
                }

                String[] numbers = line.trim().split(" ");

                for (int j = 0; j < COLUMNS; ++j) {
                    if (j < numbers.length) {
                        board[i][j] = Integer.parseInt(numbers[j]);
                        Tile tile = new Tile(typeOfMap, board[i][j], TILE_SIZE);
                        tile.setBounds(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        boardPanel.add(tile);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameLayeredPane.setPreferredSize(new Dimension(COLUMNS*TILE_SIZE, ROWS*TILE_SIZE));
        gameLayeredPane.setLayout(null);

        boardPanel.setBounds(0, 0, COLUMNS * TILE_SIZE, ROWS * TILE_SIZE);

        gameLayeredPane.add(boardPanel, 1);

        add(gameLayeredPane);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }
}