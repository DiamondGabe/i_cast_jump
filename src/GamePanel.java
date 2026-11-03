import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Magic.Spell;

import java.awt.Rectangle;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    public final int TITLE_STATE = 0;
    public final int PLAY_STATE = 1;
    public int gameState = TITLE_STATE;
    public int commandNum = 0;
    public final int screenWidth = 800;
    public final int screenHeight = 600;
    KeyHandler keyHandler = new KeyHandler();
    MouseHandler mouseHandler = new MouseHandler();
    private int fps = 90;
    private long drawInterval = 1000000000 / fps;
    private int playerX = 100;
    private int playerY = 100;
    private int playerWidth = 32;
    private int playerHeight = 64;
    private int playerSpeed = 4;
    private double velocityY = 0;
    private double gravity = 0.4;
    ArrayList<Platform> activePlatforms = new ArrayList<>();
    ArrayList<Spell> activeSpells = new ArrayList<>();


    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);
        setFocusable(true);
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        addPlatforms();
        //activePlatforms.add(new Platform(200, 450, 100, 20));
        //activePlatforms.add(new Platform(500, 420, 100, 20));
    }

    public void addPlatforms() {
        for (int i = 0; i < screenHeight; i += playerHeight+30) {
                int platformX = (int) (screenWidth * Math.random());
                int platformXWidth = (int) (Math.random() * 800);
                activePlatforms.add(new Platform(platformX, i, platformXWidth, 20));
            }
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
        if (keyHandler.escapePressed == true) {
            gameState = TITLE_STATE;
        }
        switch(gameState) {
        case TITLE_STATE:
            fps = 5;
            if (keyHandler.upPressed == true) {
                commandNum--;
                if (commandNum < 0) {
                    commandNum = 1;
                }
            }
            if (keyHandler.downPressed == true) {
                commandNum++;
                if (commandNum > 1) {
                    commandNum = 0;
                }
            }
            if (keyHandler.spacePressed == true) {
                if (commandNum == 0) {
                    fps = 90;
                    gameState = PLAY_STATE;
                } else if (commandNum == 1) {
                    System.exit(0);
                }
            }
            break;
        case PLAY_STATE:
            // Platform regeneration
            if (keyHandler.gPressed == true) {
                activePlatforms.clear();
                addPlatforms();
            }

            // Mouse tracking
            double playerCenterX = playerX + (playerWidth / 2.0);
            double playerCenterY = playerY + (playerHeight / 2.0);
            double dX = mouseHandler.mouseX - playerCenterX;
            double dY = mouseHandler.mouseY - playerCenterY;
            double angle = Math.atan2(dY, dX);

            // Spell on click
            if (mouseHandler.leftMousePressed == true) {
                Spell spell = new Spell(playerX + playerWidth / 2, playerY + playerHeight / 2, 0, 0, 10, 10, angle);
                activeSpells.add(spell);
            }

            // Spell updates
            for (int i = activeSpells.size() - 1; i >= 0; i--) {
                Spell spell = activeSpells.get(i);
                spell.update();
                // Remove spell if it goes off-screen
                if (spell.posX < 0 || spell.posX > screenWidth || spell.posY < 0 || spell.posY > screenHeight) {
                    activeSpells.remove(i);
                }
                if (getCollidingPlatform(new Rectangle(spell.posX, spell.posY, 10, 10)) != null) {
                    activeSpells.remove(i);
                }
            }

            // X-Axis movement and screen bounds
            boolean isFarLeft = playerX <= 0;
            boolean isFarRight = playerX >= screenWidth - playerWidth;
            if (keyHandler.leftPressed == true && !isFarLeft) {
                playerX -= playerSpeed;
            }
            if (keyHandler.rightPressed == true && !isFarRight) {
                playerX += playerSpeed;
            }

            // X-Axis platform collision
            Platform xHit = getCollidingPlatform(getPlayerBounds());
            if (xHit != null) {
                if (keyHandler.leftPressed == true) {
                    playerX = xHit.getX() + xHit.getWidth(); // hit left, snap to the right
                }
                if (keyHandler.rightPressed == true) {
                    playerX = xHit.getX() - playerWidth; // hit right, snap to the left
                }
            }

            // Y-Axis movement
            velocityY += gravity;
            playerY += (int) velocityY;
            boolean isGrounded = false;

            // Y-Axis platform collision
            Platform yHit = getCollidingPlatform(getPlayerBounds());
            if (yHit != null) {
                if (velocityY > 0) { // falling down since y increases downwards (I keep forgetting that)
                    isGrounded = true;
                    playerY = yHit.getY() - playerHeight;
                    velocityY = 0;
                } else if (velocityY < 0) { // jumping up
                    playerY = yHit.getY() + yHit.getHeight();
                    velocityY = 0;
                }
            }
                
            // Y-Axis screen bounds
            if (playerY >= screenHeight - playerHeight) {
                isGrounded = true;
                playerY = screenHeight - playerHeight;
                velocityY = 0;
            } else if (playerY <= 0) {
                playerY = 0;
            }

            // Jumping
            if ((keyHandler.upPressed == true || keyHandler.spacePressed == true) && isGrounded) {
                velocityY = -12;
            }

            // Sprint speed
            if (keyHandler.shiftPressed == true && isGrounded) {
                playerSpeed = 8;
            } else {
                playerSpeed = 4;
            }

            break;
        } 
        
    }

    private Platform getCollidingPlatform(Rectangle bounds) {
        for (int i = 0; i < activePlatforms.size(); i++) {
            Platform platform = activePlatforms.get(i);
            if (bounds.intersects(platform.getBounds())) {
                return platform;
            }
        }
        return null;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        switch(gameState) {
        case TITLE_STATE:
            fps = 15;
            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString("I Cast Jump", screenWidth / 2 - 120, screenHeight / 2 - 100);
            g2.setFont(new Font("Arial", Font.PLAIN, 30));
            g2.drawString("Play", screenWidth / 2 - 90, screenHeight / 2);
            g2.drawString("Quit", screenWidth / 2 - 90, screenHeight / 2 + 50);
            if (commandNum == 0) {
                g2.drawString(">", screenWidth / 2 - 120, screenHeight / 2);
            } else if (commandNum == 1) {
                g2.drawString(">", screenWidth / 2 - 120, screenHeight / 2 + 50);
            }
            break;
        case PLAY_STATE:
            g2.setColor(Color.white);
            g2.fillRect(playerX, playerY, playerWidth, playerHeight);
            for (int i = 0; i < activePlatforms.size(); i++) {
                activePlatforms.get(i).draw(g2);
            }
            for (int i = activeSpells.size() - 1; i >= 0; i--) {
                Spell spell = activeSpells.get(i);
                spell.draw(g2);
            }
            break;
        }
    }

    public Rectangle getPlayerBounds() {
        return new Rectangle(playerX, playerY, playerWidth, playerHeight);
    }
    
}