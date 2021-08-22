package theapp.core;

import theapp.input.InputHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFrame;

public class Launcher extends JFrame implements Runnable {
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    protected JPanel window;
    protected InputHandler input;
    protected int width;
    protected int height;
    protected Thread thread;
    private volatile boolean running = false;

    private synchronized void notRunning(){
        running = false;
    }
    public Launcher (String title) {
        this(title, 800, 400);
    }

    public Launcher (String title, int width, int height){
        input = new InputHandler();
        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);

        setUndecorated(true);

        setTitle(title);
        this.width = width;
        this.height = height;
        setSize(new Dimension(width, height));
        if (this.getClass() == Launcher.class)
            setBackground(Color.BLACK);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // creeate JPanel for Launcher
        if (this.getClass() == Launcher.class) {
            window = new JPanel() {
                private Image image;
                private Image btnPlayOn, btnPlayOff;
                private Image btnOptionsOn, btnOptionsOff;
                private Image btnExitOn, btnExitOff;
                private Image arrow;

                {
                    try {
                        image = ImageIO.read(Launcher.class.getResourceAsStream("/images/launcher/launcher.jpg"));
                        btnPlayOn = ImageIO.read(Launcher.class.getResourceAsStream("/images/launcher/btnPlayOn.png"));
                        btnPlayOff = ImageIO.read(Launcher.class.getResourceAsStream("/images/launcher/btnPlayOff.png"));
                        btnOptionsOn = ImageIO.read(Launcher.class.getResourceAsStream("/images/launcher/btnOptionsOn.png"));
                        btnOptionsOff = ImageIO.read(Launcher.class.getResourceAsStream("/images/launcher/btnOptionsOff.png"));
                        btnExitOn = ImageIO.read(Launcher.class.getResourceAsStream("/images/launcher/btnExitOn.png"));
                        btnExitOff = ImageIO.read(Launcher.class.getResourceAsStream("/images/launcher/btnExitOff.png"));
                        arrow = ImageIO.read(Launcher.class.getResourceAsStream("/images/launcher/arrow.png"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void paintComponent(Graphics graphics) {
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(0, 0, width, height);
                    graphics.drawImage(image, 0, 0, width, height, null);
                    drawButtons();
                }

                public void drawButtons(){
                    getGraphics().drawImage(image, 0, 0, width, height, null);
                    int xPos = width * 4 / 5 - 20;
                    drawButton(btnPlayOn, btnPlayOff, xPos + 25, height - height * 7 / 9, 70, 40, () -> play());
                    drawButton(btnOptionsOn, btnOptionsOff, xPos, height - height * 5 / 9, 120, 40, () -> options());
                    drawButton(btnExitOn, btnExitOff, xPos + 25, height - height * 3 / 9, 70, 40, () -> exit());
                }
                public void drawButton(Image buttonOn, Image buttonOff, int x, int y, int width, int height, Runnable onClick) {
                    Graphics graphics = getGraphics();
                    if (graphics == null)
                        return;
                    if (input.MouseX > x && input.MouseX < x + width && input.MouseY > y && input.MouseY < y + height){
                        graphics.drawImage(buttonOn, x, y, width, height, null);
                        graphics.drawImage(arrow, x+width+10, y+8, 22, 19, null);
                        if (input.Mousedragged)
                            onClick.run();
                    }
                    else
                        graphics.drawImage(buttonOff, x, y, width, height, null);
                    return;
                }

                private void play() {
                    App.getLauncherInstance().stopMe();
                    new App().startGame();

                }
                private void options() {
                    App.setLauncherInstance(SettingsLauncher.class, "Launcher - Options");
                }
                private void exit(){
                    App.getLauncherInstance().stopMe();
                    System.out.println(" [LOG] Exitting.");
                    System.exit(0);
                }
            };
        }
        else // create JPanel for subclasses
            window = new JPanel();
        window.setLayout(null);
        setContentPane(window);
        createComponents();
        setVisible(true);
        runMe();
    }
    public void runMe(){
        running = true;
        thread = new Thread(this, "Launcher");
        thread.start();

    }
    public void stopMe(){
        notRunning();
        this.dispose();
        try {
            if (thread.getName() != Thread.currentThread().getName())
                thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void update(){
        if (this.getClass() == Launcher.class) {
            try {
                window.getClass().getDeclaredMethod("drawButtons").invoke(window);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (input.Mousedragged == true && running == true){
            int newX = getX() + input.MouseXDiff;
            int newY = getY() + input.MouseYDiff;
            System.out.println("[LOG] Mouse press x: " + input.MousePressX + " y: " + input.MousePressY );
            System.out.println("[LOG] Last mouse press x: " + input.lastMouseX + " y: " + input.lastMouseY );
            System.out.println("[LOG] Mouse diff x: " + input.MouseXDiff + " y: " + input.MouseYDiff );
            System.out.println("[LOG] Location before: " + getX() + ", " + getY() + " and after: " + newX + ", " + newY);
            input.lastAcceptedPress(input.MouseXDiff, input.MouseYDiff);
            setLocation(newX, newY);
        }
    }
    public void run (){
        while (running){
            update();
            try {
                Thread.sleep(1000 / 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    protected void createComponents(){
        // drawn elswhere for Launcher (within JPanel typed member window)
    }
}

class SettingsLauncher extends Launcher{
    public SettingsLauncher(String title) {
        super (title, 850, 700);
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null);
    }
    @Override
    protected void createComponents(){
        Label lblResolution = new Label("Resolution");
        lblResolution.setBounds(50, 80, 80, 20);
        window.add(lblResolution);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screeSize = toolkit.getScreenSize();
        int tempWidth = (int) ( screeSize.getWidth());

        Choice dropDownRes = new Choice();
        dropDownRes.setBounds(150, 80, 150, 20);
        dropDownRes.add("480 x 360");
        dropDownRes.add("640 x 480");
        if (800 <= tempWidth) dropDownRes.add("800 x 600");
        if (960 <= tempWidth) dropDownRes.add("960 x 720");
        if (1024 <= tempWidth) dropDownRes.add("1024 x 768");
        if (1280 <= tempWidth) dropDownRes.add("1280 x 960");
        if (1400 <= tempWidth) dropDownRes.add("1400 x 1050");
        if (1440 <= tempWidth) dropDownRes.add("1440 x 1080");
        if (1600 <= tempWidth) dropDownRes.add("1600 x 1200");
        if (1856 <= tempWidth) dropDownRes.add("1856 x 1392");
        if (1920 <= tempWidth) dropDownRes.add("1920 x 1440");
        if (2048 <= tempWidth) dropDownRes.add("2048 x 1536");
        final int defaultSelection = 1;
        try {
            int index = Config.load("resolutionId");
            dropDownRes.select(index);
        } catch (NoSuchFieldException e){
            dropDownRes.select(defaultSelection);
        }
        window.add(dropDownRes);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(width - 100 , height - 70, 80, 30);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.setLauncherInstance(Launcher.class, "Game Launcher");
            }
        });
        window.add(btnCancel);

        JButton btnOK = new JButton("OK");
        btnOK.setBounds(width - 180 , height - 70, 80, 30);
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = dropDownRes.getSelectedItem();
                final String[] dimension = str.replaceAll(" ", "").split("[x]");
                App.width = Integer.parseInt(dimension[0]);
                App.height = Integer.parseInt(dimension[1]);
                Config.save("width", App.width);
                Config.save("height", App.height);
                Config.save("resolutionId", dropDownRes.getSelectedIndex(), "Resolution");
                App.setLauncherInstance( Launcher.class, "Game Launcher");
            }
        });
        window.add(btnOK);
    }
}