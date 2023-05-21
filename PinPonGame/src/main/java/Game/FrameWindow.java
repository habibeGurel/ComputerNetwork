package Game;

import Message.Message;
import Client.Client;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 * 
 * @author Habibe Gurel 1921221034
 */
public class FrameWindow extends JFrame implements WindowListener {

    public FrameWindow(String title, PinPonGame game) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(game);
        pack();
        setLocationRelativeTo(null); //centering the window
        setVisible(true);
        addWindowListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (Client.socket != null) {
            Client.Stop();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }
}
