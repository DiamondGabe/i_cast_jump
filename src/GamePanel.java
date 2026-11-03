import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    KeyHandler keyHandler = new KeyHandler();
    private int fps = 60;
    private long drawInterval = 1000000000 / fps;
    private int playerX = 100;
    private int playerY = 100;
    private int playerWidth = 32;
    private int playerHeight = 64;
    private int playerSpeed = 4;
    private double velocityY = 0;
    private double gravity = 0.4;
    ArrayList<Platform> platforms = new ArrayList<>();


    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.black);
        setDoubleBuffered(true);
        setFocusable(true);
        this.addKeyListener(keyHandler);
        platforms.add(new Platform(0, 550, 800, 50));
        platforms.add(new Platform(200, 400, 100, 20));
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
        boolean isGrounded = false;
        boolean isFarLeft = playerX <= 0;
        boolean isFarRight = playerX >= 800 - playerWidth;

        for (int i = 0; i < platforms.size(); i++) {
            Platform platform = platforms.get(i);
            if ((getPlayerBounds().intersects(platform.getBounds())) && (velocityY > 0)) {
                isGrounded = true;
                playerY = platform.getY() - playerHeight;
                velocityY = 0;
                break;
            }
        }

        if (playerY >= 600 - playerHeight) {
            isGrounded = true;
            playerY = 600 - playerHeight;
            velocityY = 0;
        }
        if ((keyHandler.upPressed == true || keyHandler.spacePressed == true) && isGrounded) {
            velocityY = -12;
        }
        if (keyHandler.leftPressed == true && !isFarLeft) {
            playerX -= playerSpeed;
        }
        if (keyHandler.rightPressed == true && !isFarRight) {
            playerX += playerSpeed;
        }
        if (keyHandler.shiftPressed == true) {
            playerSpeed = 8;
        } else {
            playerSpeed = 4;
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(playerX, playerY, playerWidth, playerHeight);
        for (int i = 0; i < platforms.size(); i++) {
            platforms.get(i).draw(g);
        }
    }

    public Rectangle getPlayerBounds() {
        return new Rectangle(playerX, playerY, playerWidth, playerHeight);
    }

    public Rectangle getPlayerLowerBounds() {
        return new Rectangle(playerX, playerY + playerHeight, playerWidth, 1);
    }
    
}