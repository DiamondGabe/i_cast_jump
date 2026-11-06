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
    public boolean facingLeft = false;
    public int speed = 2;
    public Platform base;

    public Entity(int posX, int posY, int width, int height, int maxHealth, int damage, Platform base) {
        this.x = posX;
        this.y = posY;
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.damage = damage;
        this.base = base;
        this.bounds = new Rectangle(posX, posY, width, height);
    }

    public void update() {
        if (facingLeft == false) {
                this.x += speed;
        } 
        if ((x) == (base.getX() + base.getWidth())) {
            swapDirection();
        }
        if (facingLeft == true) {
            this.x -= speed;
        } 
        if (x == base.getX()) {
            swapDirection();
        }
        
    }

    public void swapDirection() {
        facingLeft = !facingLeft;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y-height, width, height);
    }
}
