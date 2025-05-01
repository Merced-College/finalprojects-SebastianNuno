// Sebastian Nuno
// 5/1/25
// Final Project
import javax.swing.*;

public class SimplePong extends JFrame {

private int leftPlayerScore = 0;
private int rightPlayerScore = 0;

// This is to set up the paddle dimensions.
private static final int PADDLE_WIDTH = 15;
private static final int PADDLE_HEIGHT = 90;
private static final int PADDLE_EDGE_MARGIN = 40;
private static final int PADDLE_SPEED = 5;

// This is to set up the ball properties. 
private static final int BALL_SIZE = 15;
private int ballX = 400;
private int ballY = 300;
private int ballSpeedX = 0;
private int ballSpeedY = 0;

// Starting position for the Y Paddle.
private int leftPaddleY = 250;
private int rightPaddleY = 250;

// This will be the starting status of the game and for every time the game ends.
private boolean gameOver = true;

// This is to set up the keys for moving the paddles.
private boolean upPressed = false;
private boolean downPressed = false;
private boolean wPressed = false;
private boolean sPressed = false;