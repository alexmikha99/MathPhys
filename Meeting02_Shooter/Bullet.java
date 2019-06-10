import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

class Bullet {
    private int originX;
    private int originY;
    private int positionX;
    private int positionY;
    private double radius;
    private double velocityX;
    private double velocityY;
    private double windForce;
    private double windDirection;
    private double bulletMass;
    private final static double GRAVITY = 9.8;
    private double timeInitial;
    private boolean shot = false;
    private final static Color COLOR = Color.darkGray;

    public Bullet(double radius, int originX, int originY, double angle, double baseVelocity, double timeInitial, double bulletMass, double windForce, double windDirection) {
        this.radius = radius;
        this.originX = originX;
        this.originY = originY;
        this.timeInitial = timeInitial;
        this.velocityX = baseVelocity * Math.cos(angle);
        this.velocityY = baseVelocity * Math.sin(angle);
        this.bulletMass = bulletMass;
        this.windForce = windForce;
        this.windDirection = windDirection;
    }

    public void setTime(double time) {
        timeInitial = time;
    }
    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }
    public double getRadius() {
        return radius;
    }
    public void shoot() {
        shot = true;
    }

    public void stopShoot() {
        shot = false;
    }

    public boolean isShot() {
        return shot;
    }
    /**
     counting distance based on time
     in x-axis, the velocity is constant
     in y-axis, the velocity is influenced by gravitational acceleration
     and we decrease the y-displacement because of different coordinate system
     */
    public void move(double time) {
        double currentTime = time - timeInitial;
        // Force = Mass * Acceleration
        // Thus, Acceleration = Force / Mass
        velocityX += windForce * Math.cos(Math.toRadians(windDirection)) / bulletMass;
        velocityY += windForce * Math.sin(Math.toRadians(windDirection)) / bulletMass;
        positionX = (int) (originX + (velocityX * currentTime));
        positionY = (int) (originY - (velocityY * currentTime - GRAVITY * currentTime * currentTime / 2));
    }

    // drawing function
    public void draw(Graphics g) {
        int size = (int) (radius * 2);
        Graphics2D g2 = (Graphics2D) g;
        Color tempColor = g.getColor();
        g2.setColor(COLOR);

        // draw the bullet
        g2.fillOval((int) (positionX - radius), (int) (positionY - radius), size, size);
        g2.setColor(tempColor);
    }
}
