import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class PlayTetris extends JFrame implements KeyListener {
    private BufferedImage frameIcon;
    private TetrisShape sh;
    private GameOver gameOver;
    private int[] frekY;

    public PlayTetris() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        boolean endofgame = false;
        boolean playAgain = false;
        this.setBounds(0, 0, 800, 772);
        try {
            frameIcon = ImageIO.read(new File("TetrisLogo.jpg"));
        } catch (IOException e) {
            System.out.println("Hiba a logo betoltesenel!");
        }
        this.setIconImage(frameIcon);
        this.setTitle("MyTetris");

        this.setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(0, 0, 160));


        int[] y;
        int numberOfShapes = 0;
        this.setVisible(true);
        this.addKeyListener(this);

        boolean endend = false;
        while (!endend) { //Ameddig nincs vége az egész játéknak
            frekY = new int[17];
            for (int i = 0; i <= 16; i++) {
                frekY[i] = 0;
            }

            while (!endofgame) {//Ameddig nincs vége a körnek

                Random z = new Random();
                int nextShape;
                nextShape = z.nextInt(5);

                switch (nextShape) {    //random alakzat
                    case 0 -> this.sh = new Line(numberOfShapes);
                    case 1 -> this.sh = new TetrisT(numberOfShapes);
                    case 2 -> this.sh = new ItemRight(numberOfShapes);
                    case 3 -> this.sh = new ItemLeft(numberOfShapes);
                    case 4 -> this.sh = new Square(numberOfShapes);
                }

                this.add((Component) sh);

                Controller c = new Controller(sh, numberOfShapes);
                c.setFrekY(frekY);
                Thread th = new Thread(c);
                th.start();
                while (!c.isEnd()) {//Ameddig nem ér le
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                y = sh.gety();
                addToFrek(y);
                numberOfShapes = c.getN() / 4 + 1;
                frekY = c.getFrekY();
                endofgame = c.isEndofgame();
            }

            gameOver = new GameOver();
            this.add(gameOver);
            this.setVisible(true);
            while(!endend && !playAgain)//Ameddig nem nyomunk meg egy gombot addig várakozik
            {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                endend = gameOver.isClose();
                playAgain = gameOver.isPlayAgain();
            }
            if(endend){
                this.dispose();
            }
            else
                if(playAgain){
                numberOfShapes = 0;
                endofgame = false;
                gameOver.setVisible(false);
                this.remove(gameOver);
                this.setState(Frame.ICONIFIED);
                this.setState(Frame.NORMAL);
                playAgain = false;
            }
        }

    }

    private void addToFrek(int[] y) {
        for (int i = 0; i < 4; i++) {
            frekY[y[i] / 42]++;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case 40 -> sh.setFaster(true);
            case 37 -> sh.setLeft(true);
            case 39 -> sh.setRight(true);
            case 32 -> sh.setRotation(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case 40 -> sh.setFaster(false);
            case 37 -> sh.setLeft(false);
            case 39 -> sh.setRight(false);
            case 32 -> sh.setRotation(false);
        }
    }

}
