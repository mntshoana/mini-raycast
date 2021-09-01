package theapp.core;

import javax.swing.JFrame;
import java.awt.*;

public class Game extends Canvas implements Runnable {
    private JFrame frame;

    private Thread gameThread;
    private boolean isRunning = false;


    public static int width = 480;
    public static int height = width / 16 * 9;
    public static int scale = 1;

    public Game () {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Ray-casting 2.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(this);

        Dimension dimension = new Dimension(width*scale, height*scale);
        setPreferredSize(dimension);
        frame.pack(); // size up our frame to be the same as the component
        frame.setVisible(true);
    }

    public synchronized void start() {
        isRunning = true;
        gameThread = new Thread(this, "Game");
        gameThread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try{
            gameThread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            System.out.println("Success... (hwello! :)");
        }
    }

    public static void main (String[] args) {
        Game game = new Game();
        game.start();
    }
}