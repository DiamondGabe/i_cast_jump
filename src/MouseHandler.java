import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    private static final long CLICK_COOLDOWN_NANOS = 500_000_000L;
    private long lastClickTime;

    public boolean leftMousePressed;
    public boolean rightMousePressed;
    public int mouseX;
    public int mouseY;

    public MouseHandler() {
        leftMousePressed = false;
        rightMousePressed = false;
        mouseX = 0;
        mouseY = 0;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        if (button != MouseEvent.BUTTON1
                && button != MouseEvent.BUTTON3) {
            return;
        }

        long now = System.nanoTime();
        if (lastClickTime != 0 && now - lastClickTime < CLICK_COOLDOWN_NANOS) {
            return;
        }
        lastClickTime = now;

        if (button == MouseEvent.BUTTON1) {
            leftMousePressed = true;
            //System.out.println("Left mouse button pressed at (" + e.getX() + ", " + e.getY() + ")");
        } else if (button == MouseEvent.BUTTON3) {
            rightMousePressed = true;
            //System.out.println("Right mouse button pressed at (" + e.getX() + ", " + e.getY() + ")");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightMousePressed = false;
        }
    }

}
