package Game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScores implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String HIGH_SCORES_FILE = "src/HighScores.ser";
    private final String playerName;
    private final int score;
    private final String typeOfBoard;

    public HighScores(String playerName, int score, String typeOfBoard) {
        this.playerName = playerName;
        this.score = score;
        this.typeOfBoard = typeOfBoard;
    }

    public static void saveHighScores(List<HighScores> highScores){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORES_FILE));
            oos.writeObject(highScores);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Player name: " + playerName + ", Score: " + score + ", Board: " + typeOfBoard;
    }

    public static List<HighScores> loadHighScores() {
        List<HighScores> highScores = new ArrayList<>();

        File file = new File(HIGH_SCORES_FILE);

        if (!file.exists()) {
            return highScores;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            highScores = (List<HighScores>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return highScores;
    }
}