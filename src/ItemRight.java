import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ItemRight extends JPanel implements TetrisShape {
    private int[] x;
    private final int[] mapCoordX;
    private int[] y;
    private final int[] mapCoordY;
    private final int numberOfShapes;
    private boolean faster, left, right, rotation;

    public ItemRight(int nr) {

        this.numberOfShapes = nr;
        this.setBounds(140, 0, 504, 772);
        this.setBackground(new Color(199, 130, 239));
        faster = false;
        left = false;
        right = false;
        rotation = false;

        x = new int[4];
        x[0] = 252;
        x[1] = 252;
        x[2] = 294;
        x[3] = 294;
        mapCoordX = new int[numberOfShapes * 4];

        y = new int[4];
        y[0] = 0;
        y[1] = 42;
        y[2] = 42;
        y[3] = 84;
        mapCoordY = new int[numberOfShapes * 4];
        loadMap();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.pink);
        for (int i = 0; i < numberOfShapes * 4; i++) {
            g.fillRect(mapCoordX[i], mapCoordY[i], 40, 40);
        }
        g.fillRect(x[0], y[0], 40, 40);
        g.fillRect(x[1], y[1], 40, 40);
        g.fillRect(x[2], y[2], 40, 40);
        g.fillRect(x[3], y[3], 40, 40);
    }

    private void loadMap() {
        try (FileReader fReader = new FileReader("Coordinates.txt");
             BufferedReader bfReader = new BufferedReader(fReader)) {

            for (int i = 0; i < numberOfShapes * 4; i++) {
                mapCoordX[i] = Integer.parseInt(bfReader.readLine());
                mapCoordY[i] = Integer.parseInt(bfReader.readLine());
            }

        } catch (IOException f) {

            f.printStackTrace();
        }
    }

    public int[] getMapCoordX() {
        return mapCoordX;
    }

    public int[] getMapCoordY() {
        return mapCoordY;
    }


    public int[] getx() {
        return x;
    }

    public void setx(int[] x) {
        this.x = x;
    }

    public int[] gety() {
        return y;
    }

    public void sety(int[] y) {
        this.y = y;
    }

    @Override
    public boolean isFaster() {
        return faster;
    }

    @Override
    public void setFaster(boolean faster) {
        this.faster = faster;
    }


    @Override
    public boolean isLeft() {
        return left;
    }

    @Override
    public void setLeft(boolean left) {
        this.left = left;
    }

    @Override
    public boolean isRight() {
        return right;
    }

    @Override
    public void setRight(boolean right) {
        this.right = right;
    }

    @Override
    public void rotate(int i) {
        if ((i == 1 || i == 3) && x[0] > 0) {
            y[2] = y[3] = y[0];
            x[0] = x[0] - 42;
            y[0] = y[1];
            x[2] = x[1];
        } else if ((i == 1 || i == 3) && x[0] == 0) {
            y[2] = y[3] = y[0];
            y[0] = y[1];
            x[1] = x[2];
            x[3] = x[2] + 42;

        }

        if (i == 2 || i == 4) {
            x[0] = x[1];
            x[2] = x[3];
            y[0] = y[3];
            y[2] = y[1];
            y[3] = y[2] + 42;
        }
        repaint();
    }

    @Override
    public void setRotation(boolean rotation) {
        this.rotation = rotation;
    }

    public boolean isRotation() {
        return rotation;
    }

}
