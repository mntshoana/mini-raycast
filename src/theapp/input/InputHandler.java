package theapp.input;

import java.awt.event.*;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener {
    public boolean[] keyPresses = new boolean[68836];
    public int MouseX, MouseY;
    private int lastMouseX, lastMouseY;
    public int MouseXDiff, MouseYDiff;
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode > 0 && keyCode < keyPresses.length)
            keyPresses[keyCode] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode > 0 && keyCode < keyPresses.length)
            keyPresses[keyCode] = false;
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        for (int i = 0; i < keyPresses.length; i++)
            keyPresses[i] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        MouseX = e.getX();
        MouseY = e.getY();
    }

    public void captureCurrentMousePos(){
        MouseXDiff = lastMouseX - MouseX;
        if (MouseXDiff < -30)
            MouseXDiff = -30;
        if (MouseXDiff < -20)
            MouseXDiff = -20;
        if (MouseXDiff > 30)
            MouseXDiff = 30;
        if (MouseXDiff > 20)
            MouseXDiff = 20;

        MouseYDiff = lastMouseY - MouseY;
        lastMouseX = MouseX;
        lastMouseY = MouseY;
    }
}