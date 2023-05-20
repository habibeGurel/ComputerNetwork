/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Message.Message;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Habibe
 */
public class SClient {

    int id;
    String name = " ";
    Socket soket;
    ObjectOutputStream sOutput;
    ObjectInputStream sInput;
    ClientListen listenThread;
    SClient rival = null;

    public SClient(Socket gelenSoket, int id) {
        this.soket = gelenSoket;
        this.id = id;
        try {
            this.sOutput = new ObjectOutputStream(this.soket.getOutputStream());
            this.sInput = new ObjectInputStream(this.soket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.listenThread = new ClientListen(this);
    }

    public void Send(Message message) throws IOException {
        this.sOutput.writeObject(message);
    }

    public class ClientListen extends Thread {

        SClient TheClient;

        ClientListen(SClient TheClient) {
            this.TheClient = TheClient;
        }

        @Override
        public void run() {
            while (TheClient.soket.isConnected()) {
                try {
                    try {
                        if (TheClient.soket.isClosed()) {
                            System.out.println(TheClient.name + " is exited");
                            break;
                        } else {
                            Message msg = (Message) TheClient.sInput.readObject();

                            switch (msg.type) {
                                case ServerCome:
                                    TheClient.name = msg.content.toString();
                                    System.out.println("User " + TheClient.name + " has joined the server...");
                                    Server.FindCurrentRival(TheClient);
                                    break;
                                case PaddleUp:
                                    Server.PaddleUp(TheClient, msg);
                                    break;
                                case PaddleDown:
                                    Server.PaddleDown(TheClient, msg);
                                    break;
                                case PaddleStop:
                                    Server.PaddleStopped(TheClient, msg);
                                    break;
                                case ChangeScore:
                                    Server.ScoreChanged(TheClient, msg);
                                    break;
                            }
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ClientListen.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (IOException ex) {
                    Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
                    Server.clientList.remove(TheClient);
                }
            }
        }
    }

}
