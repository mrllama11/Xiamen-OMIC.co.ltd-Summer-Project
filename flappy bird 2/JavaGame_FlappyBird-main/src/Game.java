import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//import javax.sound.sampled.AudioInputStream;

class Obstacle {
    int x, y, width, height;
    Rectangle topPipe, bottomPipe;
    int distance = 110;//adjust the distance between pipes
    boolean isPassedOn = false;

    public Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        topPipe = new Rectangle(x, y, width, height);
        bottomPipe = new Rectangle(x, height + distance, width, height);
    }

    public void resetToNewPosition(int newX) {
        topPipe.x = newX;
        bottomPipe.x = newX;
        x = newX;
        topPipe.y = -(new Random().nextInt(140) + 100);
        bottomPipe.y = topPipe.y + height + distance;
        isPassedOn = false;
    }

    public boolean intersect(Rectangle rectangle) {
        return rectangle.intersects(topPipe) || rectangle.intersects(bottomPipe);
    }

    public boolean passedOn(Rectangle rectangle) {
        return rectangle.x > x + width && !isPassedOn;
    }

    public void moveX(int dx) {
        x -= dx;
        topPipe.x -= dx;
        bottomPipe.x -= dx;
    }
}

enum Direction {
    Up,
    Down,
    None
}






public class Game extends JPanel implements Runnable, MouseListener, KeyListener{

    boolean isRunning;
    Thread thread;
    BufferedImage view, background, floor, bird, tapToStartTheGame;
    BufferedImage[] flyBirdAmin;
    Rectangle backgroundBox, floorBox, flappyBox, tapToStartTheGameBox;

    int DISTORTION;
    double SCALE = 2;
    int SIZE = 256;

    int frameIndexFly = 0, intervalFrame = 10;
    Direction direction;
    float velocity = 0;
    float gravity = 0.25f;
    boolean inGame;
    BufferedImage topPipe, bottomPipe;
    Obstacle[] obstacles;
    Font font;
    int record = 0;
    int point = 0;
    private boolean gameOverFlag = false;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (view != null) {
            g.drawImage(view, 0, 0, SIZE, SIZE, null);
        }
    }
//    private void restartGame() {
//        // Reset variables
//        point = 0;
//        startPositionObstacles();
//        startPositionFlappy();
//
//        // Reset game over flag
//        gameOverFlag = false;
//
//        // Create a new JFrame for the quit screen
//        JFrame quitFrame = new JFrame("Quit");
//        quitFrame.setResizable(false);
//        quitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // Create a new panel for buttons
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Adjust vertical and horizontal gaps as desired
//
//        // Create a new quit button and register its ActionListener
//        JButton quitButton = new JButton("Quit");
//        quitButton.setFont(new Font("TimesRoman", Font.BOLD, 16));
//        quitButton.setPreferredSize(new Dimension(80, 30)); // Adjust button size as desired
//        quitButton.addActionListener(e -> System.exit(0));
//
//        // Add the quit button to the panel
//        buttonPanel.add(quitButton);
//
//        // Set the button panel as the content pane of the quit frame
//        quitFrame.setContentPane(buttonPanel);
//
//        // Resize the quit frame to the desired smaller size
//        quitFrame.pack();
//        quitFrame.setSize(200, 100); // Adjust width and height as desired
//
//        // Center the quit frame on the screen
//        quitFrame.setLocationRelativeTo(null);
//
//        // Show the quit frame
//        quitFrame.setVisible(true);
//
//        // Retrieve the parent JFrame using SwingUtilities
//        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
//        if (frame != null) {
//            // Remove the existing content pane
//            frame.getContentPane().removeAll();
//
//            // Create a new panel for buttons
//            JPanel mainPanel = new JPanel(new BorderLayout());
//            mainPanel.add(buttonPanel, BorderLayout.CENTER);
//
//            // Set the main panel as the content pane of the frame
//            frame.setContentPane(mainPanel);
//
//            // Resize the frame to fit the button panel
//            frame.pack();
//
//            // Center the JFrame on the screen
//            frame.setLocationRelativeTo(null);
//        }
//
//        // Start the game loop
//        start();
//    }

    // Game class code...


    public Game() {
        SIZE *= SCALE;
        setPreferredSize(new Dimension(SIZE, SIZE));
        addMouseListener(this);
        addKeyListener(this);

        // Set the focusable state and request focus
        setFocusable(true);
        requestFocus();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            direction = Direction.Up;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            inGame = true;
            direction = Direction.Down;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        setFocusTraversalKeysEnabled(false);
    }

    public static void main(String[] args) {
        JFrame w = new JFrame("Flappy Bird");
        w.setResizable(false);
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.add(new Game());
        w.pack();
        w.setLocationRelativeTo(null);
        w.setVisible(true);
        w.setFocusable(true);
    }


    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            isRunning = true;
            thread.start();
        }
    }
    private void restartGame() {
        // Reset variables
        point = 0;
        startPositionObstacles();
        startPositionFlappy();

        // Reset game over flag
        gameOverFlag = false;

        // Create a new JFrame for the quit screen
        JFrame quitFrame = new JFrame("Quit");
        quitFrame.setResizable(false);
        quitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a new panel with a background color
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLUE); // Set the desired background color
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Create a new panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Adjust vertical and horizontal gaps as desired

        // Create a new quit button and register its ActionListener
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("TimesRoman", Font.BOLD, 16));
        quitButton.setPreferredSize(new Dimension(80, 30)); // Adjust button size as desired
        quitButton.addActionListener(e -> System.exit(0));

        // Add the quit button to the panel
        buttonPanel.add(quitButton);

        // Add the button panel to the main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Set the main panel as the content pane of the quit frame
        quitFrame.setContentPane(mainPanel);

        // Resize the quit frame to the desired smaller size
        quitFrame.pack();
        quitFrame.setSize(200, 100); // Adjust width and height as desired

        // Center the quit frame on the screen
        quitFrame.setLocationRelativeTo(null);

        // Show the quit frame
        quitFrame.setVisible(true);

        // Retrieve the parent JFrame using SwingUtilities
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            // Remove the existing content pane
            frame.getContentPane().removeAll();

            // Set the main panel as the content pane of the frame
            frame.setContentPane(mainPanel);

            // Resize the frame to fit the content
            frame.pack();

            // Center the JFrame on the screen
            frame.setLocationRelativeTo(null);
        }

        // Start the game loop
        start();
    }

    public void start() {
        try {
            view = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
            background = ImageIO.read(getClass().getResource("/background.png"));
            floor = ImageIO.read(getClass().getResource("/floor.png"));
            tapToStartTheGame = ImageIO.read(getClass().getResource("/tap_to_start_the_game.png"));
            BufferedImage fly = ImageIO.read(getClass().getResource("/flappy_sprite_sheet.png"));
            topPipe = ImageIO.read(getClass().getResource("/top_pipe.png"));
            bottomPipe = ImageIO.read(getClass().getResource("/bottom_pipe.png"));

            flyBirdAmin = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                flyBirdAmin[i] = fly.getSubimage(i * 17, 0, 17, 12);
            }
            bird = flyBirdAmin[0];

            DISTORTION = (SIZE / background.getHeight());

            obstacles = new Obstacle[4];
            startPositionObstacles();

            int widthTapStartGame = tapToStartTheGame.getWidth() * DISTORTION;
            int heightTapStartGame = tapToStartTheGame.getHeight() * DISTORTION;
            tapToStartTheGameBox = new Rectangle(
                    (SIZE / 2) - (widthTapStartGame / 2),
                    (SIZE / 2) - (heightTapStartGame / 2),
                    widthTapStartGame,
                    heightTapStartGame
            );
            flappyBox = new Rectangle(
                    0,
                    0,
                    bird.getWidth() * DISTORTION,
                    bird.getHeight() * DISTORTION
            );
            backgroundBox = new Rectangle(
                    0,
                    0,
                    background.getWidth() * DISTORTION,
                    background.getHeight() * DISTORTION
            );
            floorBox = new Rectangle(
                    0,
                    SIZE - (floor.getHeight() * DISTORTION),
                    floor.getWidth() * DISTORTION,
                    floor.getHeight() * DISTORTION
            );


            startPositionFlappy();
            this.addMouseListener(this);

            font = new Font("TimesRoman", Font.BOLD, 16 * DISTORTION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPositionObstacles() {
        for (int i = 0; i < 4; i++) {
            obstacles[i] = new Obstacle(0, 0, topPipe.getWidth() * DISTORTION, topPipe.getHeight() * DISTORTION);
            obstacles[i].resetToNewPosition((SIZE + topPipe.getWidth() * DISTORTION) + (i * 170));
        }
    }

    public void startPositionFlappy() {
        direction = Direction.None;
        inGame = false;
        flappyBox.x = (SIZE / 2) - (flappyBox.width * 3);
        flappyBox.y = (SIZE / 2) - flappyBox.height / 2;
    }


    public void update() {
        backgroundBox.x -= 1;
        floorBox.x -= 3;

        if (backgroundBox.x + backgroundBox.getWidth() <= 0) {
            backgroundBox.x = (int) (backgroundBox.x + backgroundBox.getWidth());
        }

        if (floorBox.x + floorBox.getWidth() <= 0) {
            floorBox.x = (int) (floorBox.x + floorBox.getWidth());
        }

        intervalFrame++;
        if (intervalFrame > 5) {
            intervalFrame = 0;
            frameIndexFly++;
            if (frameIndexFly > 2) {
                frameIndexFly = 0;
            }
            bird = flyBirdAmin[frameIndexFly];
        }

        if (inGame) {
            for (Obstacle obstacle : obstacles) {
                obstacle.moveX(3);

                if (obstacle.x + obstacle.width < 0) {
                    obstacle.resetToNewPosition(SIZE + obstacle.width + 65);
                }

                if (obstacle.intersect(flappyBox)) {
                    gameOver();

                }

                if (obstacle.passedOn(flappyBox)) {
                    obstacle.isPassedOn = true;
                    point++;
                    if (point > record) {
                        record = point;
                    }
                }
            }
        }

        if (direction == Direction.Down) {
            velocity += gravity;
            flappyBox.y += velocity;
        } else if (direction == Direction.Up) {
            velocity = -3.0f;//adjust the velocity in the air
            flappyBox.y -= -velocity;
        }

        if (flappyBox.y + flappyBox.getHeight() >= SIZE - floorBox.height || flappyBox.y <= 0) {
            gameOver();
//            gameOverr();

        }
    }

    public void gameOver() {
        // Stop the game loop
        isRunning = false;

//        // Show the retry button
//        JButton retryButton = new JButton("Retry");
//        retryButton.setFont(new Font("TimesRoman", Font.BOLD, 24));
//        retryButton.addActionListener(e -> restartGame());

        // Show the quit button
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("TimesRoman", Font.BOLD, 24));
        quitButton.addActionListener(e -> System.exit(0));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 200));
//        panel.add(retryButton);
        panel.add(quitButton);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }


//    private void showGameOverMessage() {
//        // Remove the quit button (if it exists)
//        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
//        frame.getContentPane().removeAll();
//        frame.revalidate();
//        frame.repaint();
//
//        // Show pop-up message
//        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
//    }

    public void draw() {
        if (view == null) {
            view = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        }

        Graphics2D g2 = (Graphics2D) view.getGraphics();
        super.paintComponent(g2);

        g2.drawImage(
                background,
                backgroundBox.x,
                backgroundBox.y,
                (int) backgroundBox.getWidth(),
                (int) backgroundBox.getHeight(),
                null
        );
        g2.drawImage(
                background,
                (int) (backgroundBox.x + backgroundBox.getWidth()),
                backgroundBox.y,
                (int) backgroundBox.getWidth(),
                (int) backgroundBox.getHeight(),
                null
        );

        for (Obstacle obstacle : obstacles) {
            g2.drawImage(topPipe, obstacle.x, obstacle.topPipe.y, obstacle.width, obstacle.height, null);
            g2.drawImage(bottomPipe, obstacle.x, obstacle.bottomPipe.y, obstacle.width, obstacle.height, null);
        }

        g2.drawImage(
                floor,
                floorBox.x,
                floorBox.y,
                (int) floorBox.getWidth(),
                (int) floorBox.getHeight(),
                null
        );
        g2.drawImage(
                floor,
                (int) (floorBox.x + floorBox.getWidth()),
                floorBox.y,
                (int) floorBox.getWidth(),
                (int) floorBox.getHeight(),
                null
        );

        g2.drawImage(
                bird,
                flappyBox.x,
                flappyBox.y,
                (int) flappyBox.getWidth(),
                (int) flappyBox.getHeight(),
                null
        );

        if (!inGame) {
            g2.drawImage(
                    tapToStartTheGame,
                    tapToStartTheGameBox.x,
                    tapToStartTheGameBox.y,
                    (int) tapToStartTheGameBox.getWidth(),
                    (int) tapToStartTheGameBox.getHeight(),
                    null
            );
        }

        g2.setColor(Color.YELLOW);
        g2.setFont(font);
        if (!inGame) {
            g2.drawString("Record: " + record, 10, 35);
        } else {
            g2.drawString(point + "", SIZE - 80, 35);
        }

        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(view, 0, 0, SIZE, SIZE, null);
            g.dispose();
        }
    }

    @Override
    public void run() {
        try {
            requestFocus();
            start();
            while (isRunning && !gameOverFlag) {
                update();
                draw();
                Thread.sleep(1000 / 60);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // Restart the game when the retry button is clicked
        if (!inGame) {
            restartGame();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        direction = Direction.Up;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        inGame = true;
        direction = Direction.Down;
    }

    @Override
    public void mouseEntered(MouseEvent e) {


    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Restart the game when the retry button is clicked
        if (!inGame) {
            restartGame();
        }

    }

}