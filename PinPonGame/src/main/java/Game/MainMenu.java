package Game;

import Client.Client;
import Message.Message;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;

/**
 * Bilgisayar Aglari Proje 1
 * @author Habibe Gurel 1921221034
 */

public class MainMenu extends MouseAdapter {

    public static boolean isMainMenuDisplaying; // true if main menu is displaying
    public boolean isPlayerNameInput = false;
    public Font font;

    public Rectangle lbl_PlayerName;
    public boolean isPlayerSelected = false;
    public String txt_Nickname = "Nick Name->";

    public Rectangle gamerNameInput;
    public boolean isGamerNameHighlighted = false;
    public String txt_GamerName = "";

    // Ready button
    public Rectangle btn_Ready;
    public boolean isReady = false; // true if the mouse hovered over the rectangle
    public String txt_Ready = "Ready";

    //constructor
    public MainMenu(PinPonGame game) {
        isMainMenuDisplaying = true;
        game.startThread();
        font = new Font("Arial", Font.ITALIC, 30);

        // positions and dimensions of each button
        int x, y, width = 200, height = 50;

        y = PinPonGame.HEIGHT / 2 + height / 2;
        int x_flocation = PinPonGame.WIDTH / 4 - width / 2;
        int readyLoc = PinPonGame.WIDTH / 2 - width / 2;
        btn_Ready = new Rectangle(readyLoc, y, width, height);

        int x_slocation = PinPonGame.WIDTH * 3 / 4 - width / 2;
        y = PinPonGame.HEIGHT / 2 - height;//changes y position
        lbl_PlayerName = new Rectangle(x_flocation, y, width, height);

        gamerNameInput = new Rectangle(x_slocation, y, width, height);

    }

    public void draw(Graphics g) {//This draw method draws the main menu components
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(font);
        
        //main menu background settings
        Rectangle pnl_backGround = new Rectangle();
        pnl_backGround = new Rectangle(0, 0, PinPonGame.WIDTH, PinPonGame.HEIGHT);
        Color backGround = new Color(0, 0, 51);
        g.setColor(backGround);
        g2d.fill(pnl_backGround);
        
        //button color settings
        Color buttonColor = new Color(0, 0, 102);
        Color changeColor = new Color(128, 212, 255);
        Color labelColor = new Color(0, 0, 102);

        //control mechanism that changes the color of the button when the mouse is hovered over the button
        g.setColor(buttonColor);
        if (isReady) {
            g.setColor(changeColor);
        }
        g2d.fill(btn_Ready);

        // draw labels
        g.setColor(labelColor);
        g2d.fill(lbl_PlayerName);

        g.setColor(labelColor);
        g2d.fill(gamerNameInput);

        // draw borders of buttons and labels
        g.setColor(Color.CYAN);
        g2d.draw(btn_Ready);
        g2d.draw(lbl_PlayerName);
        g2d.draw(gamerNameInput);

        // draw text in buttons
        int strWidth, strHeight;
        
        strWidth = g.getFontMetrics(font).stringWidth(txt_Ready);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.LIGHT_GRAY);
        g.drawString(txt_Ready, (int) (btn_Ready.getX() + btn_Ready.getWidth() / 2 - strWidth / 2),
                (int) (btn_Ready.getY() + btn_Ready.getHeight() / 2 + strHeight / 4));

        strWidth = g.getFontMetrics(font).stringWidth(txt_Nickname);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.LIGHT_GRAY);
        g.drawString(txt_Nickname, (int) (lbl_PlayerName.getX() + lbl_PlayerName.getWidth() / 2 - strWidth / 2),
                (int) (lbl_PlayerName.getY() + lbl_PlayerName.getHeight() / 2 + strHeight / 4));

        strWidth = g.getFontMetrics(font).stringWidth(txt_GamerName);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.LIGHT_GRAY);
        g.drawString(txt_GamerName, (int) (gamerNameInput.getX() + gamerNameInput.getWidth() / 2 - strWidth / 2),
                (int) (gamerNameInput.getY() + gamerNameInput.getHeight() / 2 + strHeight / 4));

        // Draw a title on the top of the game
        Font titleFont = new Font("Arial", Font.BOLD, 50);
        String titleText = "TABLE TENNIS GAME";
        int titleWidth = g.getFontMetrics(titleFont).stringWidth(titleText);
        int titleHeight = g.getFontMetrics(titleFont).getHeight();
        g.setFont(titleFont);
        g.setColor(Color.CYAN);
        g.drawString(titleText, 250, (PinPonGame.HEIGHT / 4) - titleHeight);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        isReady = btn_Ready.contains(p);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if (btn_Ready.contains(p)) {
            isMainMenuDisplaying = false;
            Client.Start("127.0.0.1", 5000);//16.16.253.143 aws ip
            Message msg = new Message(Message.Message_Type.ServerCome);
            msg.content = txt_GamerName;
            Client.Send(msg);
        } else if (gamerNameInput.contains(p)) {
            isPlayerNameInput = true;
        }
    }

}
