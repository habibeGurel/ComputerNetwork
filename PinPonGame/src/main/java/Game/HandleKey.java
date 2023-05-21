package Game;

import Client.Client;
import java.awt.event.KeyEvent;
import Message.Message;
import java.awt.event.KeyAdapter;

/**
 * Bilgisayar Aglari Proje 1
 * @author Habibe Gurel 1921221034
 */

public class HandleKey extends KeyAdapter {

    private MainMenu mainMenu;
    private Paddle rightPaddle; // right paddle
    public static boolean rightUp = false;
    public static boolean rightDown = false;
    public static boolean leftUp = false;
    public static boolean leftDown = false;

    //constructor
    public HandleKey(Paddle p, MainMenu menu) {
        rightPaddle = p;
        mainMenu = menu;
    }

    @Override
    public void keyPressed(KeyEvent e) {//when the key pressed this method reads the key
        int keyCode = e.getKeyCode();
        if (mainMenu.txt_GamerName.length() < 8) {//the length of the name should be max 8
            if (mainMenu.isMainMenuDisplaying) {
                mainMenu.txt_GamerName += e.getKeyChar();
            }
        }
        if (keyCode == KeyEvent.VK_BACK_SPACE) {
            mainMenu.txt_GamerName = mainMenu.txt_GamerName.substring(0, mainMenu.txt_GamerName.length() - 2);
        }
        if (Client.socket != null) {
            if (keyCode == KeyEvent.VK_UP) {
                rightPaddle.changeDirections(-1);//If the up arrow key is pressed, the changeDirections() method is called, which changes the direction of the right player's paddle.
                rightUp = true;
                Message msg = new Message(Message.Message_Type.PaddleUp);
                msg.content = "Up";
                Client.Send(msg);//sends the up movement information of the player's racket on the right.
            }
            if (keyCode == KeyEvent.VK_DOWN) {//If the down arrow key is pressed, the changeDirections() method is called, which changes the direction of the right player's paddle.
                rightPaddle.changeDirections(1);
                rightDown = true;
                Message msg = new Message(Message.Message_Type.PaddleDown);
                msg.content = "Down";
                Client.Send(msg);//sends the down movement information of the player's racket on the right.
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {//stop the movement of the paddles when you stop pressing the button
        int keyCode = 0;
        if (Client.socket != null) {
            keyCode = e.getKeyCode();
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            rightDown = false;
            Message msg = new Message(Message.Message_Type.PaddleStop);
            msg.content = "Stopped";
            Client.Send(msg);
        }
        if (keyCode == KeyEvent.VK_UP) {
            rightUp = false;
            Message msg = new Message(Message.Message_Type.PaddleStop);
            msg.content = "Stopped";
            Client.Send(msg);
        }
        if (!rightUp && !rightDown) {
            rightPaddle.stop();
        }
    }

}
