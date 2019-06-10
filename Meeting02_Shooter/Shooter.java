import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
    MatFis pertemuan 2
    Note that every dimension-related are measured in pixel
    Except for angle, which is measured in radian
    Explain how parabolic motion of projectile works.
    What is the difference between mapping code in Cartesian coordinates and pixel coordinates?

    TODO:
     1. Add a text field to adjust bullet's velocity
     2. Make cannon able to shoot more than one bullet
     3. Limit the amount of bullet in the cannon
     4. Add wind force, with its direction (which impacts acceleration on x-axis and y-axis; use Newton's second law)
     5. Make a shooter game with simple moving target (yes, over-achievers, I need SIMPLE)

    Extra:
    Q: Does this mean I can make a bullet hell game for my final project?
    A: Yes, but since the concept is already explained in class, you won't get Liv's extra brownie point.
 */


class Shooter {
    private JFrame frame;
    // control panel
    private JLabel massLabel;
    private JTextField bulletMass;
    private JLabel forceLabel;
    private JTextField windForce; 
    private JLabel directionLabel;
    private JTextField windDirection;
    // game area
    private Bullet bullet = null;
    private Cannon cannon;
    private static int limit = 10; // change capacity to limit the number of bullets
    private static int shot = 0;
    private static String INSTRUCTION = "Welcome to Cannon Simulation!\n" +
            "\nMove cannon's position = W A S D\n" +
            "Move shooting direction = Left | Right \n" +
            "Launch bullet = Space\n" +
            "Try to keep the bullet inside the target\nfor as long as possible\n"+
            "Bullets left: " + Integer.toString(limit);

    private int cpSize = 230;      // set control panel's width
    public double getBulletMass() {
        return Double.parseDouble(bulletMass.getText());
    }
    public double getWindForce() {
        return Double.parseDouble(windForce.getText());
    }
    public double getWindDirection() {
        return Double.parseDouble(windDirection.getText());
    }
    public Shooter() {
        // setup the frame
        frame = new JFrame("Graphing App");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setFocusable(true);
        frame.setVisible(true);

        // setup control panel itself
        JTextArea instruction = new JTextArea(INSTRUCTION);
        instruction.setBounds(5, 5, cpSize - 5, frame.getHeight()/6);
        instruction.setEditable(false);
        frame.add(instruction);

        // Add text field for initial velocity
        JTextField initialVelocity = new JTextField();
        initialVelocity.setText("50");
        initialVelocity.setBounds(5, frame.getHeight()/6 + 5, cpSize - 5, 20);
        frame.add(initialVelocity);

        // Add text fields for bullet mass, wind force and wind direction
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(3, 2));
        massLabel = new JLabel("Bullet mass:");
        bulletMass = new JTextField("10");
        forceLabel = new JLabel("Wind force:");
        windForce = new JTextField("10");
        directionLabel = new JLabel("Wind direction:");
        windDirection = new JTextField("0");
        controls.add(massLabel);
        controls.add(bulletMass);
        controls.add(forceLabel);
        controls.add(windForce);
        controls.add(directionLabel);
        controls.add(windDirection);
        controls.setBounds(5, frame.getHeight()/6+100, cpSize - 5, frame.getHeight()/5);
        frame.add(controls);

        // setup drawing area
        DrawingArea drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), cpSize);
        cannon = new Cannon(drawingArea.GRAPH_SCALE / 2, drawingArea.getOriginX(), drawingArea.getOriginY());
        drawingArea.setCannon(cannon);
        frame.add(drawingArea);

        // Add button for shooting
        JButton focus = new JButton("Refocus");
        focus.setBounds(5, frame.getHeight()/6+50, cpSize - 5, 30);
        focus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                frame.setFocusable(true);
                frame.requestFocus();
            }
        });
        frame.add(focus);

        // Keyboard shortcuts
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        if(shot < limit) {
                            bullet = new Bullet(cannon.getBarrelWidth() / 2, (int) cannon.getBarrelMouthX(), (int) cannon.getBarrelMouthY(), cannon.getAngle(), Double.parseDouble(initialVelocity.getText()), drawingArea.getTime(), getBulletMass(), getWindForce(), getWindDirection());
                            bullet.shoot();
                            drawingArea.addBullet(bullet);
                            shot++;
                            INSTRUCTION = "Welcome to Cannon Simulation!\n" +
                            "\nMove cannon's position = W A S D\n" +
                            "Move shooting direction = Left | Right \n" +
                            "Launch bullet = Space\n" +
                            "Try to keep the bullet inside the target\nfor as long as possible\n"+
                            "Bullets left: " + Integer.toString(limit - shot);
                            instruction.setText(INSTRUCTION);
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        cannon.rotateLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        cannon.rotateRight();
                        break;
                    case KeyEvent.VK_W:
                        cannon.moveUp();
                        break;
                    case KeyEvent.VK_A:
                        cannon.moveLeft();
                        break;
                    case KeyEvent.VK_S:
                        cannon.moveDown();
                        break;
                    case KeyEvent.VK_D:
                        cannon.moveRight();
                        break;
                }
            }
        });
        // Add target
        Target target = new Target(75, frame.getWidth() * 3 / 5, drawingArea.getTime(), frame.getHeight());
        drawingArea.setTarget(target);
        drawingArea.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Shooter::new);
    }
}
