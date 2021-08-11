package theapp.core;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class Launcher extends JFrame {
    private JPanel window = new JPanel();
    private JButton btnPlay;

    private int width = 500;
    private int height = 500;
    public Launcher (){
        setTitle("Game Launcher");
        setSize(new Dimension(width, height));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        window.setLayout(null);
        getContentPane().add(window);
        setVisible(true);

        btnPlay = new JButton("Play");
        btnPlay.setBounds(width/2 - 40, height/2 - 20, 80, 40);
        btnPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new App().startGame();
            }
        });
        window.add(btnPlay);
    }
}
