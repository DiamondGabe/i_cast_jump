import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Entity {
    public int x;
    public int y;
    public int width;
    public int height;
    protected int maxHealth;
    protected int currentHealth;
    public int damage;
    private Rectangle bounds;

    public Entity(int posX, int posY, int width, int height, int maxHealth, int currentHealth, int damage) {
        this.x = posX;
        this.y = posY;
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.damage = damage;
        this.bounds = new Rectangle(posX, posY, width, height);
    }


    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void die() {
        
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, width, height);
    }
}
