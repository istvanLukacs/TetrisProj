import javax.sound.sampled.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Controller implements Runnable {

    private final TetrisShape sh;
    private int[] y, mapCoordY, frekY;
    private int[] x, mapCoordX;
    private int speed, n;
    private int currentRotation;
    private boolean end, endofgame;
    public File pewSound;
    public Clip clip;


    public Controller(TetrisShape tetrisShape, int nr) {
        this.sh = tetrisShape;
        this.n = nr * 4;
        mapCoordY = new int[n];
        mapCoordY = sh.getMapCoordY();
        y = new int[4];
        mapCoordX = new int[n];
        mapCoordX = sh.getMapCoordX();
        x = new int[4];
        frekY = new int[17];
        currentRotation = 1;
        speed = 100;
        endofgame = false;
        pewSound = new File("pew.wav");
        AudioInputStream sinput = null;
        try {
            sinput = AudioSystem.getAudioInputStream(pewSound);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        try {
            clip.open(sinput);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        y = sh.gety();
        x = sh.getx();

        deleteFromMap(); //kitorlunk egy kigyult sort

        while (y[max(y)] < 692 && (!isThere())) {
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            down();//megy lefele az alakzat

            if (sh.isLeft()) {
                toTheLeft();
            }

            if (sh.isRight()) {
                toTheRight();
            }

            if (sh.isFaster()) {
                speed = 5;
            } else {
                speed = 100;
            }

            if (sh.isRotation()) {
                sh.rotate(currentRotation);
                if (currentRotation == 4) {
                    currentRotation = 1;
                } else {
                    currentRotation++;
                }
            }

        }
        clip.start();

        end = true;
        savemap();//elmentjuk az alakzatokat
        if (y[min(y)] <= 62) {
            endofgame = true;
        }
    }

    private void deleteFromMap() {
        for (int i = 0; i <= 16; i++) {
            while (frekY[i] == 12) {
                refresh(i);
            }
        }
    }

    private void refresh(int num) {
        int i = 0;
        while (i < n) {
            if (mapCoordY[i] / 42 == num) {
                for (int j = i; j < n - 1; j++) {
                    mapCoordX[j] = mapCoordX[j + 1];
                    mapCoordY[j] = mapCoordY[j + 1];
                }
                n--;
            } else {
                i++;
            }
        }
        frekY[num] = 0;
        for (i = num - 1; i >= 0; i--) {
            for (int j = 0; j < n; j++) {
                if (mapCoordY[j] / 42 == i) {
                    mapCoordY[j] += 42;
                }
            }
        }

        for (i = 0; i < 17; i++) {
            frekY[i] = 0;
        }

        for (i = 0; i < n; i++) {
            frekY[mapCoordY[i] / 42]++;
        }
        for (i = 0; i < 4; i++) {
            frekY[y[i] / 42]++;
        }
    }

    public int getN() {
        return n;
    }

    private boolean isThere() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) {
                if (y[j] + 42 >= mapCoordY[i] && y[j] <= mapCoordY[i] + 42 && x[j] == mapCoordX[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void savemap() {

        try (FileWriter fWriter = new FileWriter("Coordinates.txt");
             BufferedWriter bfWriter = new BufferedWriter(fWriter)) {

            for (int i = 0; i < n; i++) {
                bfWriter.write(mapCoordX[i] + "\n");
                bfWriter.write(mapCoordY[i] + "\n");
            }


            for (int i = 0; i < 4; i++) {
                bfWriter.write(x[i] + "\n");
                bfWriter.write(y[i] + "\n");
            }

        } catch (IOException f) {

            f.printStackTrace();

        }
    }

    private void down() {
        for (int i = 0; i < 4; i++) {
            y[i] = y[i] + 1;
        }

        sh.sety(y);
        if (y[max(y)] >= 692) {
            int largest = max(y);
            for (int i = 3; i >= 0; i--) {
                if (i == largest) {
                    y[i] = 692;
                } else if (y[i] >= 692) {
                    y[i] = 692;
                } else if (y[i] >= 650 && y[i] < 692) {
                    y[i] = 650;
                } else if (y[i] >= 608 && y[i] < 650) {
                    y[i] = 608;
                } else {
                    y[i] = 566;
                }

            }
            sh.sety(y);
        }
        sh.repaint();
    }

    private int max(int[] array) {
        int maxi = 0;
        for (int i = 1; i < 4; i++) {
            if (array[i] > array[maxi]) {
                maxi = i;
            }
        }
        return maxi;
    }

    private int min(int[] array) {
        int maxi = 0;
        for (int i = 1; i < 4; i++) {
            if (array[i] < array[maxi]) {
                maxi = i;
            }
        }
        return maxi;
    }

    private void toTheLeft() {
        if (x[0] > 0 && !onHisLeft()) {
            for (int i = 0; i < 4; i++) {
                x[i] = x[i] - 42;
            }
            sh.setx(x);
            sh.repaint();
        }
    }

    private boolean onHisLeft() {
        for (int i = 0; i < n; i++) {
            if (isThereLeft(mapCoordX[i], mapCoordY[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean isThereLeft(int nrx, int nry) {
        int lg = min(x);
        for (int i = 0; i < 4; i++) {
            if (x[i] == x[lg]) {
                if (x[0] == nrx + 42 && ((y[i] > nry - 42 && y[i] < nry + 42))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean onHisRight() {
        for (int i = 0; i < n; i++) {
            if (isThereRight(mapCoordX[i], mapCoordY[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean isThereRight(int nrx, int nry) {
        int lg = max(x);
        for (int i = 0; i < 4; i++) {
            if (x[i] == x[lg]) {
                if (x[i] + 42 == nrx && ((y[i] > nry - 42 && y[i] < nry + 42))) {
                    return true;
                }
            }
        }
        return false;
    }


    private void toTheRight() {

        if (x[3] < 504 - 42 && !onHisRight()) {
            for (int i = 0; i < 4; i++) {
                x[i] = x[i] + 42;
            }
            sh.setx(x);
            sh.repaint();
        }
    }

    public int[] getFrekY() {
        return frekY;
    }

    public void setFrekY(int[] frekY) {
        this.frekY = frekY;
    }

    public boolean isEnd() {
        return end;
    }

    public boolean isEndofgame() {
        return endofgame;
    }
}
