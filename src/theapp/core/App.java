package theapp.core;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

import theapp.graphics.Renderer;

public class App extends Canvas implements Runnable{
    public static final int width;
    public static final int height;
    static {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screeSize = toolkit.getScreenSize();
        width = (int) ( screeSize.getWidth() * 0.50 );
        height = (int) ( screeSize.getHeight() * 0.50 );
    }

    private Thread game;
    private boolean gameRunning;

    private Renderer renderer;
    private DataBufferInt displayBuf;

    private BufferedImage mainView, cursorView;
    private BufferStrategy bufferStrategy;

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

        mainView = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        displayBuf = (DataBufferInt) mainView.getRaster().getDataBuffer();

        // Change the cursor - not to be visible
        cursorView = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorView, new Point(0,0), "blankCursor");
        frame.getContentPane().setCursor(blankCursor);

        renderer = new Renderer(this);
    }

    public void run() {
        System.out.println("Game up and running.");

        final long second = 1000000000; // nano seconds per second
        long prevTime = System.nanoTime();

        // Count frames and refresh screen
        int frame = 0;
        for (int i = 0; gameRunning; i++) {
            requestFocus();
            long currentTime = System.nanoTime();
            if (currentTime - prevTime > second) {
                System.out.println(" [LOG] " + frame + " fps.");
                fps = frame + "FPS";
                prevTime += second;
                frame = 0; // reset
            }
            update();
            frame++;
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

        renderer.update();

        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(mainView, 0,0,width,height,null);
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

    public int[] getBuffer(){
        return displayBuf.getData();
    }
    
    public static void main(String[] args){
        Launcher app = new Launcher();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        app.dispose();
        App mainApp = new App(); // Soon to be graphics component
        mainApp.startGame();
        return;
    }
}