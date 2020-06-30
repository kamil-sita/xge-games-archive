package interactions.listeners;

import java.awt.event.KeyEvent;

final public class KeyListener implements java.awt.event.KeyListener {

    private boolean[] keysPressed = new boolean[128];

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < keysPressed.length) {
            keysPressed[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < keysPressed.length) {
            keysPressed[e.getKeyCode()] = false;
        }
    }

    public boolean isKeyPressed(int keyNumber) {
        return keysPressed[keyNumber];
    }

}
