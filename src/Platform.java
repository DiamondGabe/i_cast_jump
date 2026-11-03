import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Platform {
    private int x;
    private int y;
    private int width;
    private int height;
    private Rectangle bounds;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(x, y, width, height);
    }
    
}
