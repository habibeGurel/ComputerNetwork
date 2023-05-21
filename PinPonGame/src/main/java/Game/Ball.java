package Game;

import Client.Client;
import java.awt.Color;
import java.awt.Graphics;
import Message.Message;
/**
 * 
 * @author Habibe Gurel 1921221034
 */

//The ball class of the table tennis game
public class Ball {

    public static final int SIZE = 16;
    int x, y, motionX, motionY, ballSpeed = 5;

    public Ball() {
        clear();
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void drawBall(Graphics g) {
        g.setColor(Color.white);
        g.fillRoundRect(x, y, SIZE, SIZE, 30, 30);
    }

    private void clear() {
        // set up the ball position
        x = PinPonGame.WIDTH / 2 - SIZE / 2;
        y = PinPonGame.HEIGHT / 2 - SIZE / 2;

        motionX = PinPonGame.BallIndıcator(Math.random() * 2.0 - 1);
        motionY = PinPonGame.BallIndıcator(Math.random() * 2.0 - 1);
    }

    public void checkCollision(Paddle rightPaddle , Paddle  leftPaddle) {
        // checkCollision the position
        x += motionX * ballSpeed;
        y += motionY * ballSpeed;

        //ceiling and floor collisions
        if (y + SIZE >= PinPonGame.HEIGHT || y <= 0) {
            changeYDirection();
        }
        if (x <= 0) { // left wall
            clear();
        }
        // wall collisions
        if (x + SIZE >= PinPonGame.WIDTH) { // isRight wall
            rightPaddle.addPoint();
            Message msg = new Message(Message.Message_Type.ChangeScore);
            msg.content = rightPaddle.score;
            Client.Send(msg);//send to server
            clear();
        }
    }

    public void changeYDirection() {
        motionY *= -1;
    }

    public void changeXDirection() {
        motionX *= -1;
    }

}
