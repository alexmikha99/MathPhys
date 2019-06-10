import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

class Target {
    private int positionX;
    private int positionY;
    private double radius;
    private double timeInitial;
    private double screenHeight;
    private static Color COLOR = Color.RED;

    public Target(double radius, int screenX, double timeInitial, double screenHeight) {
        this.radius = radius;
        this.positionX = screenX;
        this.timeInitial = timeInitial;
        this.screenHeight = screenHeight;
    }
    public double getRadius() {
        return radius;
    }
    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }
    public void setTime(double time) {
        timeInitial = time;
    }
    public Color getColor() {
        return COLOR;
    }
    public void changeColor(Color color) {
        COLOR = color;
    }
    /**
     counting distance based on time
     target x stays in place
     target y moves up and down
     */
    public void move(double time) {
        double currentTime = time - timeInitial;
        positionY = (int)(Math.abs(Math.sin(currentTime/3)) * screenHeight*3/4 + screenHeight*1/8);
    }

    // drawing function
    public void draw(Graphics g) {
        int size = (int) (radius * 2);
        Graphics2D g2 = (Graphics2D) g;
        Color tempColor = g.getColor();
        g2.setColor(COLOR);

        // draw the target
        g2.fillOval((int) (positionX - radius), (int) (positionY - radius), size, size);
        g2.setColor(tempColor);
    }
}
