package theapp.core;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

import theapp.graphics.DisplayBuffer;

public class App extends Canvas implements Runnable{
    public static final int width = 1280;
    public static final int height = 720;

    private Thread action;
    private boolean threadRunning;
    private DisplayBuffer display;

    private BufferedImage bufferedImage;
    private DataBufferInt image;
    private BufferStrategy bufferStrategy;

    private static final long uid = 1;
    public App(){
        JFrame frame = new JFrame();
        frame.setTitle("Mini Raycast Engine");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.add(this);
        frame.setVisible(true);

        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image = (DataBufferInt) bufferedImage.getRaster().getDataBuffer();

        display = new DisplayBuffer(width, height);
    }

    public void run() {
        threadRunning = true;
        System.out.println("Game up and running.");

        final int fps = 60;
        final long hertz = 1000000000 / fps; // 1 sec = 1 billion nano seconds
        for (int i = 0; threadRunning ; i++) {
            long time = System.nanoTime();
            update();
            long updateTime = System.nanoTime() - time;
            long milliSecInterval = (hertz - updateTime) / 1000;
            try {
                if (milliSecInterval > 0)
                    Thread.sleep(milliSecInterval);
            } catch (InterruptedException e) {
                System.out.println(" [Error] Something went wrong with the action thread.");
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("Game reaches end.");
    }
    private void startGame() {
        if (threadRunning)
            return; // running just once for now

        action = new Thread(this);
        action.start();
    }

    private void update(){
        bufferStrategy = this.getBufferStrategy();// from Canvas
        if (bufferStrategy == null){
            createBufferStrategy(3);
            return;
        }

        display.update();
        for (int i = 0; i < width * height; i++){
            image.getData()[i] = display.displayMemory[i];
        }
        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(bufferedImage, 0,0,width,height,null);
        graphics.dispose();
        bufferStrategy.show();

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