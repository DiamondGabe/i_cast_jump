package Magic;

import java.awt.Color;
import java.awt.Graphics;

public class Spell {
    public int damage;
    public int posX;
    public int posY;
    public double velocityX;
    public double velocityY;
    public double gravity = 0.2;
    public boolean detonating = false;

    public Spell (int startX, int startY, int velocityX, int velocityY, int damage, int speed, double angle) {
        this.damage = damage;
        this.posX = startX;
        this.posY = startY;
        this.velocityX = Math.cos(angle) * speed;
        this.velocityY = Math.sin(angle) * speed;
    }

    public void update() {
        velocityY += gravity;
        posX += velocityX;
        posY += velocityY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.PINK);
        g.fillOval(posX, posY, 10, 10);
    }

    public void effects(Graphics g) {
        if (detonating) {
            g.setColor(Color.RED);
            g.fillOval(posX, posY, 30, 30);
        }
    }

    public void detonate() {
        detonating = true;
    }

}
