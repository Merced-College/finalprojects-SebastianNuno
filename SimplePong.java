// Sebastian Nuno
// 5/1/25
// Final Project
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class SimplePong extends JPanel {
    // Ball Class
    static class Ball {
        private int x, y, vx, vy;
        private static final int RADIUS = 10;

        public Ball(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        public void move() {
            x += vx;
            y += vy;
        }

        public void bounceVertical() {
            vy = -vy;
        }

        public void bounceHorizontal() {
            vx = -vx;
        }

        public int getX() { return x; }
        public int getY() { return y; }
        public int getRadius() { return RADIUS; }
        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Paddle Class
    static class Paddle {
        private int x, y, width, height;
        private static final int PADDLE_WIDTH = 20;
        private static final int PADDLE_HEIGHT = 100;

        public Paddle(int x, int y) {
            this.x = x;
            this.y = y;
            this.width = PADDLE_WIDTH;
            this.height = PADDLE_HEIGHT;
        }

        public void moveUp() {
            if (y > 0) y -= 5;
        }

        public void moveDown() {
            if (y + height < 600) y += 5;
        }

        public int getX() { return x; }
        public int getY() { return y; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
    }

    // GameState Class (for scores and ball reset)
    static class GameState {
        public int player1Score;
        public int player2Score;
        public Ball ball;

        public GameState() {
            this.player1Score = 0;
            this.player2Score = 0;
            this.ball = new Ball(400, 300, 5, 5);
        }

        public void resetBall() {
            ball.setPosition(400, 300);
        }
    }

    // Linked List for ball history
    static class BallHistory {
        private Node head;

        public BallHistory() {
            head = null;
        }

        public void append(int x, int y) {
            Node newNode = new Node(x, y);
            newNode.next = head;
            head = newNode;
        }

        public void printPositions() {
            Node current = head;
            while (current != null) {
                System.out.println("Ball Position: (" + current.x + ", " + current.y + ")");
                current = current.next;
            }
        }

        private class Node {
            int x, y;
            Node next;

            public Node(int x, int y) {
                this.x = x;
                this.y = y;
                this.next = null;
            }
        }
    }

    // StackGameState Class (for storing game state history)
    static class StackGameState {
        private Stack<GameState> gameStateStack;

        public StackGameState() {
            gameStateStack = new Stack<>();
        }

        public void pushState(GameState gameState) {
            gameStateStack.push(gameState);
        }

        public GameState popState() {
            if (!gameStateStack.isEmpty()) {
                return gameStateStack.pop();
            }
            return null;
        }
    }

    // Main Game Class with logic, collision detection, etc.
    private Paddle paddleLeft, paddleRight;
    private Ball ball;
    private GameState gameState;
    private StackGameState stateStack;
    private BallHistory ballHistory;

    public SimplePong() {
        this.paddleLeft = new Paddle(50, 250);
        this.paddleRight = new Paddle(730, 250);
        this.gameState = new GameState();
        this.stateStack = new StackGameState();
        this.ballHistory = new BallHistory();
        this.ball = gameState.ball; // Initialize ball in the game

        // Listen to key events for paddle movement
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) paddleLeft.moveUp();
                if (e.getKeyCode() == KeyEvent.VK_S) paddleLeft.moveDown();
                if (e.getKeyCode() == KeyEvent.VK_UP) paddleRight.moveUp();
                if (e.getKeyCode() == KeyEvent.VK_DOWN) paddleRight.moveDown();
            }
        });
    }

    // Game loop method
    public void gameLoop() {
        Timer timer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBall();
                checkCollisions();
                updateDisplay();
                repaint();
            }
        });
        timer.start();
    }

    private void moveBall() {
        // Save current game state before ball moves
        stateStack.pushState(new GameState()); // Save before movement
        ball.move();
        ballHistory.append(ball.getX(), ball.getY());
    }

    private void checkCollisions() {
        // Collision with top/bottom
        if (ball.getY() <= 0 || ball.getY() >= 590) {
            ball.bounceVertical();
        }

        // Collision with paddles
        if (ball.getX() <= paddleLeft.getX() + paddleLeft.getWidth() && ball.getY() >= paddleLeft.getY() && ball.getY() <= paddleLeft.getY() + paddleLeft.getHeight()) {
            ball.bounceHorizontal();
        }
        if (ball.getX() >= paddleRight.getX() - ball.getRadius() && ball.getY() >= paddleRight.getY() && ball.getY() <= paddleRight.getY() + paddleRight.getHeight()) {
            ball.bounceHorizontal();
        }

        // Scoring
        if (ball.getX() <= 0) {
            gameState.player2Score++;
            gameState.resetBall();
        }
        if (ball.getX() >= 800) {
            gameState.player1Score++;
            gameState.resetBall();
        }
    }

    private void updateDisplay() {
        // Displaying the scores
        System.out.println("Player 1: " + gameState.player1Score + " Player 2: " + gameState.player2Score);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 600);

        // Draw paddles and ball
        g.fillRect(paddleLeft.getX(), paddleLeft.getY(), paddleLeft.getWidth(), paddleLeft.getHeight());
        g.fillRect(paddleRight.getX(), paddleRight.getY(), paddleRight.getWidth(), paddleRight.getHeight());
        g.fillOval(ball.getX(), ball.getY(), ball.getRadius() * 2, ball.getRadius() * 2);
    }

    public static void main(String[] args) {
        // Setting up the game window
        JFrame frame = new JFrame("Pong Game");
        SimplePong game = new SimplePong();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.setVisible(true);

        // Start the game loop
        game.gameLoop();
    }
}

