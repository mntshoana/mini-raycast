package theapp.core;

import java.awt.Canvas;
import javax.swing.JFrame;
public class App extends Canvas implements Runnable{
    private static final long uid = 1;

    public static final int width = 1280;
    public static final int height = 720;

    private Thread action;
    private boolean threadRunning;

    public App(){
        JFrame frame = new JFrame();
        frame.setTitle("Mini Raycast Engine");
        frame.setSize(width, height);

        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.setVisible(true);
    }

    public void run() {
        threadRunning = true;
        System.out.println("Game up and running.");
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(" [Error] Something went wrong with the action thread.");
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("Game reaches end.");
        threadRunning = false;
    }
    private void startGame() {
        if (threadRunning)
            return; // running just once for now

        action = new Thread(this);
        action.start();
    }
    private void stopGame(){
        if (threadRunning == false)
            return; // do nothing
        try {
            action.join();
        }
        catch (Exception e){
            System.out.println(" [Error] Failed to join thread.");
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static void main(String[] args){
        App app = new App(); // Soon to be graphics component
        app.startGame();

        return;
    }
}