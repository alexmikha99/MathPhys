import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.lang.Math;
import java.util.ArrayList;

class DrawingArea extends JPanel {
    public final static int GRAPH_SCALE = 30;
    private double time = 0;
    private final static double TIME_INCREMENT = 0.05;
    private int width;
    private int height;
    private int originX;        // the origin points (0, 0)
    private int originY;
    private int lengthX;        // how many numbers shown along absis and ordinate
    private int lengthY;
    private int score = 0;
    private Image drawingArea;
    private Thread animator;    // thread to draw the
    private Cannon cannon;
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private Target target;

    // setup the drawing area
    public DrawingArea(int width, int height, int cpSize) {
        super(null);
        this.width = width - cpSize;
        this.height = height;
        setBounds(cpSize, 0, this.width, this.height);
        drawingArea = createImage(this.width, this.height);

        originX = this.width / 4;
        originY = this.height / 4;
        lengthX = (this.width - originX) / GRAPH_SCALE;
        lengthY = (this.height - originY) / GRAPH_SCALE;

        // trigger drawing process
        drawingArea = createImage(this.width, this.height);
        animator = new Thread(this::eventLoop);
    }

    public void start() {
        animator.start();
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }
    public void setTarget(Target target) {
        this.target = target;
    }
    public void addBullet(Bullet bullet) {
        this.bullets.add(bullet);
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public double getTime() {
        return time;
    }

    private void eventLoop() {
        drawingArea = createImage(width, height);
        while (true) {
            update();
            render();
            printScreen();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }

    private void update() {
        time += TIME_INCREMENT;
        for(int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i) != null && bullets.get(i).isShot()) {
                bullets.get(i).move(time);
                Bullet cur = bullets.get(i);
                double distance = Math.pow(cur.getPositionX() - target.getPositionX(), 2) + Math.pow(cur.getPositionY() - target.getPositionY(), 2);
                double rsum = Math.pow(cur.getRadius() + target.getRadius(), 2);
                
                // If the distance between bullet and target is smaller than radius of bullet + radius of target
                if(distance <= rsum)  {
                    score++;
                    target.changeColor(Color.GREEN);
                }
                else target.changeColor(Color.RED);
    
                if (bullets.get(i).getPositionY() > getHeight()) {
                    bullets.get(i).stopShoot();
                }
            }
        }
        target.move(time);
    }

    private void render() {
        if (drawingArea != null) {
            //get graphics of the image where coordinate and function will be drawn
            Graphics g = drawingArea.getGraphics();

            // clear screen
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.black);
            //draw the x-axis and y-axis
            g.drawLine(0, originY, getWidth(), originY);
            g.drawLine(originX, 0, originX, getHeight());

            // draw the score
            g.drawString("Score: " + Integer.toString(score), 10, 20);
            //print numbers on the x-axis and y-axis, based on the scale
            for (int i = 0; i < lengthX; i++) {
                g.drawString(Integer.toString(i), (originX + (i * GRAPH_SCALE)), originY);
                g.drawString(Integer.toString(-1 * i), (originX + (-i * GRAPH_SCALE)), originY);
            }
            for (int i = 0; i < lengthY; i++) {
                g.drawString(Integer.toString(-1 * i), originX, (originY + (i * GRAPH_SCALE)));
                g.drawString(Integer.toString(i), originX, (originY + (-i * GRAPH_SCALE)));
            }

            // draw cannon, bullet, target
            cannon.draw(g);
            target.draw(g);
            for(int i = 0; i < bullets.size(); i++) {
                if (bullets.get(i) != null && bullets.get(i).isShot()) {
                    bullets.get(i).draw(g);
                }
            }
        }
    }

    private void printScreen()
    {
        try
        {
            Graphics g = getGraphics();
            if(drawingArea != null && g != null)
            {
                g.drawImage(drawingArea, 0, 0, null);
            }

            // Sync the display on some systems.
            // (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        catch(Exception ex)
        {
            System.out.print("Graphics error: ");
            ex.printStackTrace();
        }
    }
}
