import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameOver extends JPanel {

    private BufferedImage gameOverImage;
    private boolean close, playAgain;

    public GameOver() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        close = false;
        playAgain = false;
        this.setBounds(0, 0, 800, 772);
        this.setBackground(new Color(0,0,160));
        try {
            gameOverImage = ImageIO.read(new File("GameOver.jpg"));
        } catch (IOException e) {
            System.out.println("Hiba a jatek vege betoltesenel!");
        }

        JButton restart = new JButton("Játsz újra");
        restart.setBackground(new Color(255, 127, 39));
        restart.setBounds(350, 650,100,50);
        this.add(restart);

        JButton exit = new JButton("Kilépés");
        exit.setBackground(new Color(255, 127, 39));
        this.add(exit);

        JLabel picLabel = new JLabel(new ImageIcon(gameOverImage));
        this.add(picLabel);
        File tetrisSound = new File("gameOver.wav");
        AudioInputStream sinput = AudioSystem.getAudioInputStream(tetrisSound);
        Clip clip = AudioSystem.getClip();

        clip.open(sinput);
        clip.start();


        restart.addActionListener(e -> {

            clip.stop();
            playAgain = true;
        });

        exit.addActionListener(e -> close = true);

        this.setVisible(true);
    }

    public boolean isClose() {
        return close;
    }

    public boolean isPlayAgain() {
        return playAgain;
    }

}
