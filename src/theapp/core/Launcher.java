package theapp.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class Launcher extends JFrame {
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    protected JPanel window = new JPanel();
    protected int width;
    protected int height;
    public Launcher (String title, int width, int height){
        setTitle(title);
        this.width = width;
        this.height = height;
        setSize(new Dimension(width, height));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        window.setLayout(null);
        getContentPane().add(window);
        createComponents();
        setVisible(true);
    }
    protected void createComponents(){
        JButton btnPlay = new JButton("Play");
        btnPlay.setBounds(width/2 - 40, height/2 - 40 -30, 80, 40);
        btnPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new App().startGame();
            }
        });
        JButton btnOptions = new JButton("Options");
        btnOptions.setBounds(width/2 - 40, height /2 - 40 + 30, 80, 40);
        btnOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SettingsLauncher("Launcher - Options");
            }
        });
        window.add(btnPlay);
        window.add(btnOptions);
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

        Choice dropDownRes = new Choice();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screeSize = toolkit.getScreenSize();
        int tempWidth = (int) ( screeSize.getWidth());
        int tempHeight = (int) ( screeSize.getHeight());
        dropDownRes.setBounds(150, 80, 150, 20);
        if (480 <= tempWidth) dropDownRes.add("480 x 360");
        if (640 <= tempWidth) dropDownRes.add("640 x 480");
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
        dropDownRes.select(1);
        window.add(dropDownRes);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(width - 100 , height - 70, 80, 30);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Launcher("Game Launcher", 500, 500);
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
                dispose();
                new Launcher("Game Launcher", 500, 500);
            }
        });
        window.add(btnOK);
    }
}