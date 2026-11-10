import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import Magic.Spell;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class Player {
    GamePanel gp;
    KeyHandler keyH;
    MouseHandler mouseH;

    public int x, y;
    public int width, height;
    public int speed;
    public int maxHealth;
    public int currentHealth;
    public double velocityY = 0;
    public double gravity = 0.4;
    public int spellCooldown = 0;
    public Rectangle bounds = new Rectangle(x, y, width, height);
    private BufferedImage playerSprite;
    private boolean facingLeft;

    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseH) {
        this.gp = gp;
        this.keyH = keyH;
        this.mouseH = mouseH;
        setDefaultValues();
        try {
            playerSprite = ImageIO.read(new File("res/wizard.png"));
        } catch (Exception e) {
            System.out.println("Could not load player sprite!");
            e.printStackTrace();
        }
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        width = 32;
        height = 64;
        speed = 4;
        maxHealth = 100;
        currentHealth = maxHealth;
    }

    public void update() {
        // Mouse tracking
        double playerCenterX = x + (width / 2.0);
        double playerCenterY = y + (height / 2.0);
        double dX = mouseH.mouseX - playerCenterX;
        double dY = mouseH.mouseY - playerCenterY;
        double angle = Math.atan2(dY, dX);

        // Spell on click
        if ((mouseH.leftMousePressed == true) && (spellCooldown == 0)) {
            Spell spell = new Spell(x + width / 2, y + height / 2, 0, 0, 10, 10, angle);
            gp.activeSpells.add(spell);
            spellCooldown = 20;
        }
        if (spellCooldown > 0) {
            spellCooldown--;
        }

        // Entity/Player Collision
        Entity collidor = gp.getCollidingEntity(getBounds());
        if (collidor != null) {
            currentHealth -= collidor.damage;
        }

        // X-Axis movement and screen bounds
        boolean isFarLeft = x <= 0;
        boolean isFarRight = x >= gp.screenWidth - width;
        if (keyH.leftPressed == true && !isFarLeft) {
            x -= speed;
            facingLeft = true;
        }
        if (keyH.rightPressed == true && !isFarRight) {
            x += speed;
            facingLeft = false;
        }

        // X-Axis platform collision
        Platform xHit = gp.getCollidingPlatform(getBounds());
        if (xHit != null) {
            if (keyH.leftPressed == true) {
                x = xHit.getX() + xHit.getWidth(); // hit left, snap to the right
            }
            if (keyH.rightPressed == true) {
                x = xHit.getX() - width; // hit right, snap to the left
            }
        }

        // Y-Axis movement
        velocityY += gravity;
        y += (int) velocityY;
        boolean isGrounded = false;

        // Y-Axis platform collision
        Platform yHit = gp.getCollidingPlatform(getBounds());
        if (yHit != null) {
            if (velocityY > 0) { // falling down
                isGrounded = true;
                y = yHit.getY() - height;
                velocityY = 0;
            } else if (velocityY < 0) { // jumping up
                y = yHit.getY() + yHit.getHeight();
                velocityY = 0;
            }
        }
            
        // Y-Axis screen bounds
        if (y >= gp.screenHeight - height) {
            isGrounded = true;
            y = gp.screenHeight - height;
            velocityY = 0;
        } else if (y <= 0) {
            y = 0;
        }

        // Jumping
        if ((keyH.upPressed == true || keyH.spacePressed == true) && isGrounded) {
            velocityY = -12;
            Sound jump_sound = new Sound();
            jump_sound.loadFile("res/jump.wav");
            jump_sound.setVolume(150);
            jump_sound.play();
        }

        // Sprint speed
        if (keyH.shiftPressed == true && isGrounded) {
            speed = 8;
        } else {
            speed = 4;
        }
    }

    public void draw(Graphics2D g2) {
        // Draw Player
        if (playerSprite != null) {
            if (facingLeft == true) {
                g2.drawImage(playerSprite, x, y, width, height, null);
            } else {g2.drawImage(playerSprite, x+width, y, -width, height, null);}
        } else {
            // A fallback just in case the image failed to load, so you aren't invisible
            g2.setColor(Color.white);
            g2.fillRect(x, y, width, height);
        }

        // Draw Health Bar
        int healthBarValue = currentHealth;
        if (healthBarValue < 0) {healthBarValue = 0;}
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(x - width, y - 20, maxHealth, 10);
        g2.setColor(Color.RED);
        g2.fillRect(x - width, y - 20, healthBarValue, 10);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
