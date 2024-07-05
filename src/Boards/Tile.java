package Boards;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tile extends JPanel{
    private final int TILE_SIZE;
    private final String typeOfMap;
    private final JLabel label;

    public Tile(String typeOfMap, int textureNumber, int TILE_SIZE){
        this.TILE_SIZE = TILE_SIZE;
        this.typeOfMap = typeOfMap;
        this.label = new JLabel();

        setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        ImageIcon icon = getIconForNum(textureNumber, typeOfMap);
        label.setIcon(icon);

        add(label, BorderLayout.CENTER);
    }

    private ImageIcon getIconForNum(int number, String typeOfMap){
        String path = "src/Textures/Boards/";

        path += switch (typeOfMap){
            case "SmallGB" -> "SmallGB/";
            case "MediumGB" -> "MediumGB/";
            case "LargeGB" -> "LargeGB/";
            case "ExtraLargeGB" -> "ExtraLargeGB/";
            default -> "GiganticGB/";
        };

        /*
            Explanation of numbers:
            0 - empty tile
            1 - path with pellet
            2 - path with energizer
            3 - wall
            4 - gates
         */

        String imagePath =  switch (number){
            case 1 -> path + "Pellet.png";
            case 2 -> path + "Energizer.png";
            case 3 -> path + "Wall.png";
            case 4 -> path + "Gates.png";
            default -> path + "EmptyTile.png";
        };

        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            Image scaledImg = img.getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_FAST);
            return new ImageIcon(scaledImg);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public void setValue(int textureNumber) {
        ImageIcon icon = getIconForNum(textureNumber, typeOfMap);
        label.setIcon(icon);
        revalidate();
    }
}
