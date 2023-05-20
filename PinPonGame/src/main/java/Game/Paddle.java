package Game;

import static Game.PinPonGame.gamerNameTxt;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 *
 * @author Habibe
 *
 */
public class Paddle {

    private Color color; // color of the paddle
    private int x, y; // positions
    private int velocityP = 0; // speed and direction of paddle
    private int speed = 10; // speed of the paddle movement
    private int height = 85, width = 22; // dimensions
    public int score = 0; // score for the player
    private boolean right; // true if it's the right paddle

    public Paddle(Color c, boolean right) {
        // initial properties
        color = c;
        this.right = right;

        if (right) // different x values if right or left paddle
        {
            x = PinPonGame.WIDTH - width;
        } else {
            x = 0;
        }
        y = PinPonGame.HEIGHT / 2 - height / 2;
    }

    public void addPoint() {
        score++;
    }

    public void draw(Graphics g) {

        // draw paddle
        g.setColor(color);
        g.fillRect(x, y, width, height);

        // draw score
        int str_Xpos; // x position of the string
        String scoreText ="  Score: "+ Integer.toString(score);
        Font font = new Font("Arial", Font.BOLD, 40);

        if (right) {
            str_Xpos = PinPonGame.WIDTH / 2 + 25;
        } else {
            int strWidth = g.getFontMetrics(font).stringWidth(scoreText); // we need the width of the string so we can
            str_Xpos = PinPonGame.WIDTH / 2 - 25 - strWidth;
        }

        g.setFont(font);
        g.drawString(scoreText, str_Xpos, 50);
    }

    public void update(Ball b) {//makes update the ball position
        // collisions
        int ballX = b.getX();
        int ballY = b.getY();

        //this line updates the ball position
        y = PinPonGame.provideRange(y + velocityP, 0, PinPonGame.HEIGHT - height);

        if (right) {
            if (ballX + Ball.SIZE >= x && ballY + Ball.SIZE >= y && ballY <= y + height) {
                b.changeXDirection();
            }
        } else {
            if (ballX <= width + x && ballY + Ball.SIZE >= y && ballY <= y + height) {
                b.changeXDirection();
            }
        }
    }

    public void changeDirections(int direction) {//when the direction is -1 the paddle will be down, when it is 1 paddle will be up
        velocityP = speed * direction;
    }

    public void stop() {//stop the paddle movements
        velocityP = 0;
    }

}
