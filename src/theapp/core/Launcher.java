package theapp.core;

import javax.swing.*;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Launcher extends JFrame {
    public Launcher (){
        setTitle("Game Launcher");
        setSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
