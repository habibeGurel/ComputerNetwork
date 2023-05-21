package Game;

import Client.Client;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.io.IOException;
import java.net.Socket;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferStrategy;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 *
 * @author Habibe
 *
 */
//class that manages the game, drawing and updating physics
public class PinPonGame extends Canvas implements Runnable {

    public MainMenu menu; // Main Menu object
    private static final long serialVersionUID = 1L;

    public boolean isGameFinished = false;

    public final static int WIDTH = 1000;
    public final static int HEIGHT = WIDTH * 9 / 16; // 16:9 aspect ratio

    public boolean running = false; // true if the game is running
    public Thread gameThread; // thread where the game is updated AND drawn (single thread game)

    public Paddle rightPdl;
    public static Paddle leftPdl;
    public Ball ball;

    public Rectangle gamerName;
    public static String gamerNameTxt = "";

    public Rectangle rivalName;
    public static String rivalNameTxt = ".......";//If the rival hasn't arrived yet, there is "....."

    public Rectangle winnerName;
    public static String winnerNameTxt = "";

    Font font; // for player names
    Font font1; // for winner

    //constructor
    public PinPonGame() {

        createCanvas();
        new FrameWindow("Table Tennis Game", this);
        initializeGame();

        // position and dimensions of buttons
        int x, y, width = 100, height = 40;
        y = height / 2;

        int x_first = width / 2;
        rivalName = new Rectangle(x_first, y, width, height);

        int x_second = PinPonGame.WIDTH - 3 * width / 2;
        gamerName = new Rectangle(x_second, y, width, height);

        int x_third = PinPonGame.WIDTH / 3;
        int y_third = PinPonGame.HEIGHT / 2 - 20;
        int w1 = 200;
        int h1 = 80;
        winnerName = new Rectangle(x_third, y_third, w1, h1);

        font = new Font("Arial", Font.BOLD, 20);//rival's font
        font1 = new Font("Arial", Font.BOLD, 50);

        this.addMouseListener(menu);
        this.addMouseMotionListener(menu);
        this.setFocusable(true);

    }

    public void myDraw() {
        BufferStrategy myBuffer = this.getBufferStrategy();
        if (myBuffer == null) {// control if it is empty
            this.createBufferStrategy(3); // Creating a Triple Buffer
            return;
        }
        Graphics g = myBuffer.getDrawGraphics();
        renderBackground(g);

        // myDraw paddles (score will be drawn with them)
        rightPdl.draw(g);
        leftPdl.draw(g);

        // myDraw ball
        ball.drawBall(g);

        // myDraw main menu components
        if (menu.isMainMenuDisplaying) {
            menu.draw(g);
        }
        gamerNameTxt = "Player 1: " + menu.txt_GamerName;

        g.dispose(); // Releases system resources and cleans up this graphics context by marking it as no longer in use.
        myBuffer.show();
    }

    private void createCanvas() {//configure the canvas
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
    }

    public synchronized void startThread() {
        gameThread = new Thread(this);
        gameThread.start(); // start thread
        running = true;
    }

    public void stopThread() {
        try {
            gameThread.join(); // waits the thread to finish
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initializeGame() {//start the game
        // start and set up paddle objects
        Color leftPaddleColor = new Color(255, 51, 0);
        Color rightPaddleColor = new Color(255, 153, 51);
        rightPdl = new Paddle(leftPaddleColor, true);
        leftPdl = new Paddle(rightPaddleColor, false);

        ball = new Ball();// start Ball object

        menu = new MainMenu(this);// start main menu

        this.addKeyListener(new HandleKey(rightPdl, menu));
    }

    @Override
    public void run() {
        this.requestFocus();
        double tickCount = 60.0;
        // game timer
        long lastTime = System.nanoTime();
        double ns = 1000000000 / tickCount;
        double delta = 0;//delta is used to drive scheduling and checkCollision operations in the game loop.
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                myUpdate();
                delta--;
                myDraw();
                frames++;
            }
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
        stopThread();
    }

    private void renderBackground(Graphics g) {//All image-related adjustments are made here.
        //table of the game color settings
        Color tennisTable = new Color(0, 68, 102);//like a dark blue
        g.setColor(tennisTable);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        
        // line line of the tennis table that in the middle
        Stroke line = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0);
        g2d.setStroke(line);
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        
        g2d.setStroke(new BasicStroke(2f));
        // Line of the tennis Table that in the middle of the paddles
        g2d.drawOval(WIDTH / 2 - 150, HEIGHT / 2 - 150, 300, 300);
        g.setColor(Color.BLACK);
        // circle of the tennis Table that in the middle
        g.drawLine(0, HEIGHT / 2, WIDTH, HEIGHT / 2);
        g.setColor(Color.WHITE);
        
        if (!menu.isMainMenuDisplaying) {// After connect to server
            g.setFont(font);//set the font

            //configure the gamer name color and location settings
            int str_Width, str_Height;//string width and height
            str_Width = g.getFontMetrics(font).stringWidth(gamerNameTxt);
            str_Height = g.getFontMetrics(font).getHeight();

            g.setColor(Color.BLACK);
            g.drawString(gamerNameTxt, (int) (gamerName.getX() + gamerName.getWidth() / 2 - str_Width / 2),
                    (int) (gamerName.getY() + gamerName.getHeight() / 2 + str_Height / 4));

            //configure the rival name color and location settings
            str_Width = g.getFontMetrics(font).stringWidth(rivalNameTxt);
            str_Height = g.getFontMetrics(font).getHeight();

            g.setColor(Color.BLACK);
            g.drawString(rivalNameTxt, (int) (rivalName.getX() + rivalName.getWidth() / 2 - str_Width / 2),
                    (int) (rivalName.getY() + rivalName.getHeight() / 2 + str_Height / 4));

            if (isGameFinished) {//when the game finished, there is a winner of the game, so this control mechanism prints the winner when the game is over
                g.setFont(font1);

                int strWidth1, strHeight1;
                strWidth1 = g.getFontMetrics(font).stringWidth(winnerNameTxt);
                strHeight1 = g.getFontMetrics(font).getHeight();
                //writes the winner
                g.setColor(Color.RED);
                g.drawString(winnerNameTxt, (int) (winnerName.getX() + winnerName.getWidth() / 2 - strWidth1 / 2),
                        (int) (winnerName.getY() + winnerName.getHeight() / 2 + strHeight1 / 4));
            }
        }
    }

    //update locations of all components like ball, paddle and checkCollision the other settings
    public void myUpdate() {
        if (!menu.isMainMenuDisplaying) {
            if (Client.isRivalFound) {
                if (isGameFinished == false) {
                    ball.checkCollision(rightPdl, leftPdl);
                    rightPdl.update(ball);
                    leftPdl.update(ball);
                    //when the score is at least 10, the player is the winner
                    if (leftPdl.score >= 10) {
                        isGameFinished = true;
                        winnerNameTxt = "LOST :( ";
                    } else if (rightPdl.score >= 10) {
                        isGameFinished = true;
                        winnerNameTxt = "YOU WIN";
                    }
                }
            }
        }
    }

    public static int BallIndıcator(double d) {//used to determine the position of the ball.
        if (d <= 0) {
            return -1;
        }
        return 1;
    }

    public static int provideRange(int value, int min, int max) {//method keeps the value between the min and max
        return Math.min(Math.max(value, min), max);
    }

    public static void main(String[] args) {//starts the game
        new PinPonGame();
    }

}
