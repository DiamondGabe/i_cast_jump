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
    Sound backgroundMusic = new Sound();
    private int fps = 90;
    private long drawInterval = 1000000000 / fps;
    public Player player = new Player(this, keyHandler, mouseHandler);
    ArrayList<Platform> activePlatforms = new ArrayList<>();
    ArrayList<Spell> activeSpells = new ArrayList<>();
    ArrayList<Entity> activeEnemies = new ArrayList<>();



    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);
        setFocusable(true);
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        addPlatforms();
        backgroundMusic.loadFile("res/I_Cast_Jump_Theme.wav");
        backgroundMusic.loop();
        //activePlatforms.add(new Platform(200, 450, 100, 20));
        //activePlatforms.add(new Platform(500, 420, 100, 20));
    }

        public void addPlatforms() {
        for (int i = 0; i < screenHeight; i += player.height+30) {
                int platformX = (int) (screenWidth * Math.random());
                int platformXWidth = (int) (Math.random() * 800);
                Platform form = new Platform(platformX, i, platformXWidth, 20);
                activePlatforms.add(form);
                summonEnemy(platformX, i, form);
            }
    }

    public void summonEnemy(int x, int y, Platform b) {
        activeEnemies.add(new Entity(x, y, player.width, player.height, 20, 10, b));
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
            drawInterval = 1000000000L / fps;
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
            fps = 15;
            if (keyHandler.upPressed == true) {
                commandNum--;
                if (commandNum < 0) {
                    commandNum = 2;
                }
            }
            if (keyHandler.downPressed == true) {
                commandNum++;
                if (commandNum > 2) {
                    commandNum = 0;
                }
            }
            if (keyHandler.spacePressed == true) {
                if (commandNum == 0) {
                    fps = 90;
                    gameState = PLAY_STATE;
                } else if (commandNum == 2) {
                    System.exit(0);
                }
            }
            break;
        case PLAY_STATE:
            // Platform regeneration
            if (keyHandler.gPressed == true) {
                activePlatforms.clear();
                activeEnemies.clear();
                addPlatforms();
            }

            /*// Mouse tracking
            double playerCenterX = player.x + (player.width / 2.0);
            double playerCenterY = player.y + (player.height / 2.0);
            double dX = mouseHandler.mouseX - playerCenterX;
            double dY = mouseHandler.mouseY - playerCenterY;
            double angle = Math.atan2(dY, dX);

            // Spell on click
            if ((mouseHandler.leftMousePressed == true) && (player.spellCooldown == 0)) {
                Spell spell = new Spell(player.x + player.width / 2, player.y + player.height / 2, 0, 0, 10, 10, angle);
                activeSpells.add(spell);
                player.spellCooldown = 20; 
                if (player.spellCooldown > 0) {
                    player.spellCooldown--;
                }
            }*/

            // Spell updates
            for (int i = activeSpells.size() - 1; i >= 0; i--) {
                Spell spell2 = activeSpells.get(i);
                spell2.update();
                // Remove spell if it goes off-screen
                if (spell2.posX < 0 || spell2.posX > screenWidth || spell2.posY < 0 || spell2.posY > screenHeight) {
                    activeSpells.get(i).detonate();
                    activeSpells.remove(i);
                }
                if (getCollidingPlatform(new Rectangle(spell2.posX, spell2.posY, 10, 10)) != null) {
                    activeSpells.get(i).detonate();
                    activeSpells.remove(i);
                }
                // If spell hits an enemy
            }

            // Enemy updates
            for (int i = activeEnemies.size() - 1; i >= 0; i--) {
                Entity enemy = activeEnemies.get(i);
                enemy.update();
                // Remove Enemy if it goes off-screen
                if (enemy.x < 0 || enemy.x > screenWidth || enemy.y < 0 || enemy.y > screenHeight) {
                    activeEnemies.get(i).swapDirection();
                }
            }

            /*// Entity/Player Collision
            Entity collidor = getCollidingEntity(player.bounds);
            if (collidor != null) {
                player.currentHealth -= collidor.damage;
            }*/

            // Update Player
            player.update();

            break;
            }
        
    }

    public Platform getCollidingPlatform(Rectangle bounds) {
        for (int i = 0; i < activePlatforms.size(); i++) {
            Platform platform = activePlatforms.get(i);
            if (bounds.intersects(platform.getBounds())) {
                return platform;
            }
        }
        return null;
    }

    public Entity getCollidingEntity(Rectangle bounds) {
        for (int i = activeEnemies.size() - 1; i >= 0; i--) {
            Entity enemy = activeEnemies.get(i);
            if (bounds.intersects(enemy.getBounds())) {
                System.out.println("Enemy Collided at: " + enemy.x + enemy.y);
                return enemy;
            }
        }
        return null;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        switch(gameState) {
        case TITLE_STATE:
            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString("I Cast Jump", screenWidth / 2 - 120, screenHeight / 2 - 100);
            g2.setFont(new Font("Arial", Font.PLAIN, 30));
            g2.drawString("Play", screenWidth / 2 - 90, screenHeight / 2);
            g2.drawString("Options", screenWidth / 2 - 90, screenHeight / 2 + 50);
            g2.drawString("Quit", screenWidth / 2 - 90, screenHeight / 2 + 100);
            g2.drawString(">", screenWidth / 2 - 120, screenHeight / 2 + (50 * commandNum));
            break;
        case PLAY_STATE:

            player.draw(g2);

            // Draw Platforms
            for (int i = 0; i < activePlatforms.size(); i++) {
                activePlatforms.get(i).draw(g2);
            }

            // Draw Spells
            for (int i = activeSpells.size() - 1; i >= 0; i--) {
                Spell spell = activeSpells.get(i);
                spell.draw(g2);
                spell.effects(g);
            }

            // Draw Enemies
            for (int i = activeEnemies.size() - 1; i >= 0; i--) {
                activeEnemies.get(i).draw(g2);
            }


            

            break;
        }
    }

    public Rectangle getPlayerBounds() {
        return player.bounds;
    }
    
}
