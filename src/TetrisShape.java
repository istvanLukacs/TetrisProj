public interface TetrisShape {

    void rotate(int i);

    int[] getx();

    void setx(int[] x);

    int[] gety();

    void sety(int[] y);

    void repaint();

    boolean isFaster();

    void setFaster(boolean faster);

    boolean isLeft();

    void setLeft(boolean left);

    boolean isRight();

    void setRight(boolean right);

    void setRotation(boolean rotation);

    boolean isRotation();

    int[] getMapCoordX();

    int[] getMapCoordY();

}
