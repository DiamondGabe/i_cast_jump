import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private Thread thread;
    private boolean running;
    private int fps = 60;
    private long targetTime = 1000 / fps;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void run() {
        running = true;
        long startTime;
        long elapsed;
        long wait;

        while (running) {
            startTime = System.nanoTime();

            update();
            repaint();

            elapsed = System.nanoTime() - startTime;
            wait = targetTime - elapsed / 1000000;

            if (wait < 0) wait = 5;

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        // Update game logic here
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw game elements here
    }
    
}
