package theapp.core;

import theapp.entity.Player;
import theapp.graphics.Particle;
import theapp.graphics.VisualBuffer;
import theapp.input.Keyboard;
import theapp.input.Mouse;
import theapp.level.Level;
import theapp.level.LoadedLevel;
import theapp.level.location.Coordinate;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

public class Game extends Canvas implements Runnable {
    private JFrame frame;
    private final String TITLE = "Ray-casting 2.0";
    private BufferedImage privateBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private VisualBuffer screen;
    private Keyboard keyboard;
    private Mouse mouse;
    private Player player;
    private Thread gameThread;
    private boolean isRunning = false;

    private Level level;
    public static int width = 480;
    public static int height = width / 16 * 9;
    public static int scale = 1;

    public Game () {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(this);

        screen = new VisualBuffer(width, height);

        Dimension dimension = new Dimension(width*scale, height*scale);
        setPreferredSize(dimension);
        frame.pack(); // size up our frame to be the same as the component
        frame.setVisible(true);

        keyboard = new Keyboard();
        mouse = new Mouse();
        addKeyListener(keyboard);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        level = Level.level1;

        Coordinate pCoordinate = new Coordinate(0, 0);
        player = new Player(keyboard, pCoordinate.x(), pCoordinate.y());
        player.initLevel(level);
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
        long lastNanoTime = System.nanoTime();
        long secondTimer = System.currentTimeMillis();
        double delta = 0.0;
        final double fractionalUpdate = 1000000000.0/60;
        // fps counter
        int framesCounter = 0; // how many framesCounter the computer can actually run
        int updatesCounter = 0; // how many times upadte() is called

        requestFocus();
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastNanoTime) / fractionalUpdate;
            lastNanoTime = now;
            while (delta >= 1) {
                // game update limiter: 60 calculation max per second
                //System.out.println("Success... (hwello 60 times a second! :)");
                update();// handles logic and calculations
                updatesCounter++;
                delta--;
            }
            render(); // redraw (basically as fast as we can)
            framesCounter++;

            if (System.currentTimeMillis() - secondTimer > 1000){
                // if a second has passed
                // display fps
                secondTimer += 1000; // add a second
                frame.setTitle(TITLE + "   |   " + updatesCounter + " ups " + framesCounter + " fps");
                System.out.println(updatesCounter + " ups");
                System.out.println(framesCounter + " fps");
                updatesCounter = framesCounter = 0; // reset counters
            }
        }
        stop();
    }

    private int yUpdater = 1;
    public  void update() {
        level.update();
        keyboard.update();
        player.update();
    }

    public void render() {
        BufferStrategy buffer = getBufferStrategy(); // Canvas already has one
        if (buffer == null){
            createBufferStrategy(3);
            return;
        }

        // probably clear screen here
        for (int i = 0; i < screen.pixels.length; i++)
            screen.pixels[i] = 0;
        // screen.renderToBuffer(xOff,yOff);
        // move level to centre player
        int xMoved = player.x - screen.getWidth()/2;
        int yMoved = player.y - screen.getHeight()/2;
        level.render(xMoved, yMoved, screen);
        player.render(screen);

        Random random = new Random();
        Particle particle  = new Particle(2, 2, 0xffffffff);
        for (int i = 0; i < 100; i++){
            int x = random.nextInt(20);
            int y = random.nextInt(20);
            screen.renderParticleToBuffer(width - 60 + x, 50 + y, particle, true);
        }
        int privateBufRef[] = ((DataBufferInt)(privateBuffer.getRaster().getDataBuffer())).getData();
        for (int i = 0; i < screen.pixels.length; i++)
            privateBufRef[i] = screen.pixels[i];
        Graphics g = buffer.getDrawGraphics();
        g.drawImage(privateBuffer, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        buffer.show(); // blitting
    }

    public static void main (String[] args) {
        Game game = new Game();
        game.start();
    }
}