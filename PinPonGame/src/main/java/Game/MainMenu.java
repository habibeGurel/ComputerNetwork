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
 * the main menu at the start of the game
 *
 * @author Habibe
 *
 */
public class MainMenu extends MouseAdapter {

    public static boolean available; // true if main menu is displaying
    private boolean isPlayerName = false;
    private Font font;
    
    private Rectangle lbl_PlayerName;
    private boolean isPlayer = false;
    private String txt_Nickname = "Nick Name->";
    
    private Rectangle gamerName;
    private boolean tHighlight = false;
    public String txt_GamerName = "";
   
    // Ready button
    private Rectangle btn_Ready;
    private boolean isReady = false; // true if the mouse hovered over the rectangle
    private String txt_Ready = "Ready";
  
    //constructor
    public MainMenu(PinPonGame game) {
        available = true;
        game.startThread();
        font = new Font("Arial", Font.ITALIC, 30);
        
        // position and dimensions of each button
        int x, y, width=200, height=50;

        y = PinPonGame.HEIGHT / 2 + height / 2;
        int x_flocation = PinPonGame.WIDTH / 4 - width / 2;
        int readyLoc = PinPonGame.WIDTH / 2 - width / 2;
        btn_Ready = new Rectangle(readyLoc, y, width, height);

        int x_slocation = PinPonGame.WIDTH * 3 / 4 - width / 2;
        y = PinPonGame.HEIGHT / 2 - height;//change y position
        lbl_PlayerName = new Rectangle(x_flocation, y, width, height);

        gamerName = new Rectangle(x_slocation, y, width, height);
        
    }

    public void draw(Graphics g) {//This myDraw method draws the main menu components
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(font);

        //button color settings
        Color buttonColor = new Color(0, 119, 179);
        Color changeColor = new Color(128, 212, 255);
        Color labelColor = new Color(0, 119, 179);

        //control mechanism that changes the color of the button when the mouse is hovered over the button
        g.setColor(buttonColor);
        if (isReady) {
            g.setColor(changeColor);
        }
        g2d.fill(btn_Ready);

        // myDraw labels
        g.setColor(labelColor);
        g2d.fill(lbl_PlayerName);

        g.setColor(labelColor);
        g2d.fill(gamerName);

        // myDraw borders
        g.setColor(Color.white);
        g2d.draw(btn_Ready);
        g2d.draw(lbl_PlayerName);
        g2d.draw(gamerName);

        // myDraw text in buttons
        int strWidth, strHeight;

        // Play Button text
        strWidth = g.getFontMetrics(font).stringWidth(txt_Ready);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.LIGHT_GRAY);
        g.drawString(txt_Ready, (int) (btn_Ready.getX() + btn_Ready.getWidth() / 2 - strWidth / 2),
                (int) (btn_Ready.getY() + btn_Ready.getHeight() / 2 + strHeight / 4));

        // User Name Label text
        strWidth = g.getFontMetrics(font).stringWidth(txt_Nickname);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.LIGHT_GRAY);
        g.drawString(txt_Nickname, (int) (lbl_PlayerName.getX() + lbl_PlayerName.getWidth() / 2 - strWidth / 2),
                (int) (lbl_PlayerName.getY() + lbl_PlayerName.getHeight() / 2 + strHeight / 4));

        // User Name Text Content
        strWidth = g.getFontMetrics(font).stringWidth(txt_GamerName);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.LIGHT_GRAY);
        g.drawString(txt_GamerName, (int) (gamerName.getX() + gamerName.getWidth() / 2 - strWidth / 2),
                (int) (gamerName.getY() + gamerName.getHeight() / 2 + strHeight / 4));

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();

        if (btn_Ready.contains(p)) {
            available = false;
            Client.Start("127.0.0.1", 5000);
            Message msg = new Message(Message.Message_Type.ServerCome);
            msg.content = txt_GamerName;
            Client.Send(msg);
        } else if (gamerName.contains(p)) {
            isPlayerName = true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        // determine if mouse is hovering inside one of the buttons
        isReady = btn_Ready.contains(p);

    }

}
