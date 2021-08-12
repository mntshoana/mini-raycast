package theapp.core;

import javax.swing.*;
import java.awt.Dimension;
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
        JButton btnOK = new JButton("OK");
        btnOK.setBounds(width - 100 , height - 70, 80, 30);
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Launcher("Game Launcher", 500, 500);
            }
        });
        window.add(btnOK);
    }
}