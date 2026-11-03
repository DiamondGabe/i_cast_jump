import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    public boolean shiftPressed;
    public boolean spacePressed;
    public boolean escapePressed;

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_W) {
            upPressed = true;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_S) {
            downPressed = true;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_A) {
            leftPressed = true;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_D) {
            rightPressed = true;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SHIFT) {
            shiftPressed = true;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
            spacePressed = true;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
            escapePressed = true;
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_W) {
            upPressed = false;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_S) {
            downPressed = false;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_A) {
            leftPressed = false;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_D) {
            rightPressed = false;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
            spacePressed = false;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
            escapePressed = false;
        }
    }
}
