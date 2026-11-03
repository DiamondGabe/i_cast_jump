import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Rectangle;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    KeyHandler keyHandler = new KeyHandler();
    private int fps = 60;
    private long drawInterval = 1000000000 / fps;
    private int playerX = 100;
    private int playerY = 100;
    private int playerSpeed = 4;
    private double velocityY = 0;
    private double gravity = 0.4;

    

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.black);
        setDoubleBuffered(true);
        setFocusable(true);
        this.addKeyListener(keyHandler);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /*
    public void addNotify() {
        super.addNotify();
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    } 
    */

    public void run() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / (double) drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

        }
    }

    private void update() {
        // System.out.println("Ticking"); // Debug
        velocityY += gravity;
        playerY += (int) velocityY;
        boolean isGrounded = playerY >= 600 - 32;
        boolean isFarLeft = playerX <= 0;
        boolean isFarRight = playerX >= 800 - 32;

        if (isGrounded) {
            playerY = 600 - 32;
            velocityY = 0;
        }
        if (keyHandler.upPressed == true && isGrounded) {
            velocityY = -12;
        }
        if (keyHandler.leftPressed == true && !isFarLeft) {
            playerX -= playerSpeed;
        }
        if (keyHandler.rightPressed == true && !isFarRight) {
            playerX += playerSpeed;
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(playerX, playerY, 32, 32);
    }

    public Rectangle getPlayerBounds() {
        return new Rectangle(playerX, playerY, 32, 32);
    }
    
}