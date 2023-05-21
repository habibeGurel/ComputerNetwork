package Client;

import Game.PinPonGame;

import java.net.Socket;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import Message.Message;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bilgisayar Aglari Proje 1
 * @author Habibe Gurel 1921221034
 */
public class Client {

    public static Socket socket;
    public static ObjectInputStream sInput;//object required to retrieve data
    public static ObjectOutputStream sOutput;//object required to send data
    public static Listen listenServer; //listenning server
    public static boolean isRivalFound;

    public static void Start(String IP, int port) {
        try {
            //client socket object
            Client.socket = new Socket(IP, port);
            Client.Display("Connected to Server...");
            // input stream
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            // output stream
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listenServer = new Listen();
            Client.listenServer.start();

            //the first message is a name of the player
            Message msg = new Message(Message.Message_Type.RivalFound);
            msg.content = PinPonGame.gamerNameTxt;
            Client.Send(msg);
            System.out.println("Player :" + PinPonGame.gamerNameTxt);//Last connected player's name is written
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Stop() {//this method makes client stop
        try {
            if (Client.socket != null) {//Terminates all connections to stop client socket as long as it exists
                Client.listenServer.stop();
                Client.socket.close();
                Client.sOutput.flush();
                Client.sOutput.close();

                Client.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Send(Message msg) {// this method sends the message to other client
        try {
            Client.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Display(String msg) {
        System.out.println(msg);
    }

}

class Listen extends Thread {

    public void run() {
        //when socket connected
        while (Client.socket.isConnected()) {
            try {
                Message received = (Message) (Client.sInput.readObject());

                switch (received.type) {
                    case RivalFound://When the rival found this case will be run
                        PinPonGame.rivalNameTxt = "Player 2: " + received.content.toString();
                        Client.isRivalFound = true;//the boolean will be true
                        break;
                    case PaddleUp:
                        PinPonGame.leftPdl.changeDirections(-1);
                        break;
                    case PaddleDown:
                        PinPonGame.leftPdl.changeDirections(1);
                        break;
                    case PaddleStop:
                        PinPonGame.leftPdl.stop();
                        break;
                    case ChangeScore:
                        PinPonGame.rightPdl.score = Integer.parseInt(received.content.toString());
                        break;
                }

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                break;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }

    }
}
