package Meeting03_Dribble;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;

/*
    MatFis pertemuan 3
    Collision between parabolically moving object against wall

    TODO:
     0. Review about elastic and inelastic collisions. What happened when you change the coefficient of resistution (COR)?
     1. Add more balls with different colors, sizes, and velocities
     2. Create UI to add new balls and delete some instances
     3. Add COR field to the UI, so user can choose between using different COR than the default or not
     4. Turn all balls into linearly moving ones (apply Newton's first law here).
     5. Create diagonal walls and modify the calculation to adjust with diagonal walls
     6. Create UI to customize the walls
 */

public class Dribble {
    private JFrame frame;
    private DrawingArea drawingArea;

    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();

    public Dribble() {
        //configure the main canvas
        frame = new JFrame("Dribbling Balls");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // create the walls
        createWalls();

        // create the ball
        balls.add(new Ball(300, 200, 50, 10, 10, Color.blue));
        balls.add(new Ball(300, 100, 20, 3, -3, Color.green));

        drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), balls, walls);
        frame.add(drawingArea);
        drawingArea.start();
    }

    private void createWalls() {
        // vertical wall must be defined in clockwise direction
        // horizontal wall must be defined in counter clockwise direction

        walls.add(new Wall(1300, 100, 50, 100, Color.black));	// horizontal top
        walls.add(new Wall(50, 600, 1300, 600, Color.black));  // horizontal bottom
        walls.add(new Wall(1300, 100, 1300, 600, Color.black));  // vertical right
        walls.add(new Wall(50, 600, 50, 100, Color.black));  // vertical left
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Dribble::new);
    }
}
