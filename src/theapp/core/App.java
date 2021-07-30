package theapp.core;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

import theapp.graphics.DisplayBuffer;

public class App extends Canvas implements Runnable{
    public static final int width;
    public static final int height;
    static {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screeSize = toolkit.getScreenSize();
        width = (int) ( screeSize.getWidth() * 0.80 );
        height = (int) ( screeSize.getHeight() * 0.80 );
    }

    private Thread game;
    private boolean gameRunning;
    private DisplayBuffer display;

    private BufferedImage bufferedImage, cursor;
    private DataBufferInt image;
    private BufferStrategy bufferStrategy;

    private static final long uid = 1;

    private String fps = "";
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

        // Change the cursor to not be visible
        cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "blankCursor");
        frame.getContentPane().setCursor(blankCursor);

        display = new DisplayBuffer(width, height, this);
    }

    public void run() {
        System.out.println("Game up and running.");

        final long second = 1000000000; // nano seconds per second
        long prevTime = System.nanoTime();
        int frameCount = 0;
        for (int i = 0; gameRunning; i++) {
            requestFocus();
            long currentTime = System.nanoTime();
            if (currentTime - prevTime > second) {
                System.out.println(" [LOG] " + frameCount + " fps.");
                fps = frameCount + "FPS";
                prevTime += second;
                frameCount = 0;
            }
            update();
            frameCount++;
        }
        System.out.println("Game reaches end.");
    }
    public void startGame() {
        if (gameRunning)
            return; // running just once for now

        game = new Thread(this);
        game.start();
        gameRunning = true;
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
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Akzidenz Grotesk", 0, 50));
        graphics.drawString(fps, 20, 50);


        graphics.dispose();
        bufferStrategy.show();

    }

    public void stopGame(){
        if (gameRunning == false)
            return; // do nothing
        try {
            game.join();
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