package Boards;

import Characters.Creature;

import javax.swing.*;
import java.awt.*;

public abstract class GameBoard extends JFrame {
    protected int TILE_SIZE;
    protected int COLUMNS;
    protected int ROWS;
    protected int[][] board;
    protected JLayeredPane gameLayeredPane;
    protected JPanel creature;
    protected JPanel boardPanel;
    protected String typeOfMap;

    public GameBoard() {
        this.TILE_SIZE = 0;
        this.COLUMNS = 0;
        this.ROWS = 0;
        setBackground(Color.BLACK);

        setTitle("Pac-Man");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.gameLayeredPane = new JLayeredPane();
        this.creature = new JPanel();
    }

    protected abstract void setUpBoard();

    public String getTypeOfBoard(){
        return typeOfMap;
    }

    public int getTile(int tileX, int tileY){
        if (tileX >= 0 && tileX < COLUMNS && tileY >= 0 && tileY < ROWS) {
            return board[tileY][tileX];
        } else {
            return -1;
        }
    }

    public int getTileSize(){
        return TILE_SIZE;
    }

    public void addCreature(Creature creature){
        creature.setBounds(0, 0, TILE_SIZE, TILE_SIZE);
        creature.setBackground(new Color(0, 0, 0, 0));
        creature.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        creature.setOpaque(false);

        gameLayeredPane.add(creature, 0);

        revalidate();
    }

    public void removeCreature(Creature creature){
        gameLayeredPane.remove(creature);

        gameLayeredPane.remove(boardPanel);
        gameLayeredPane.add(boardPanel, 1);

        revalidate();
    }

    public void setTile(int tileX, int tileY, int value){
        if(tileX >= 0 && tileX < COLUMNS && tileY >= 0 && tileY < ROWS) {
            board[tileY][tileX] = value;

            Tile tile = (Tile) boardPanel.getComponent(tileY * COLUMNS + tileX);
            tile.setValue(value);

            tile.revalidate();
        }
    }

    public int getColumns(){
        return COLUMNS;
    }

    public int getRows(){
        return ROWS;
    }

    public JLayeredPane getGameLayeredPane(){
        return gameLayeredPane;
    }

    public int[][] getBoard() {
        return board;
    }

    public synchronized void setBoard(int[][] board) {
        if(board != null){
            this.board = board;

            if (boardPanel != null) {
                boardPanel.removeAll();

                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        Tile tile = new Tile(typeOfMap, board[i][j], TILE_SIZE);
                        tile.setBounds(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);boardPanel.add(tile);
                    }
                }
            }
        }
    }
}
