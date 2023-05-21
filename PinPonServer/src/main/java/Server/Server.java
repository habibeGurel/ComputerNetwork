package Server;

import java.net.ServerSocket;
import java.util.ArrayList;
import Message.Message;
import Message.Message.Message_Type;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bilgisayar Aglari Proje 1
 * @author Habibe Gurel 1921221034
 */

public class Server {

    public static ServerSocket socketServer;
    public static int portNum = 0; //port number
    public static int CId = 0; //client ID
    public static ServerThread runThread;
    public static ArrayList<SClient> clientList = new ArrayList<>();
    public static Semaphore pairTwo = new Semaphore(1, true);

    public static void Start(int portopen) {
        try {
            Server.portNum = portopen;
            Server.socketServer = new ServerSocket(Server.portNum);

            Server.runThread = new ServerThread();
            Server.runThread.start();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void FindCurrentRival(SClient c) {//It browses the client list and assigns the client it finds as rival.
        for (SClient client : clientList) {
            if (c != client && client.rival == null) {
                c.rival = client;
                client.rival = c;
                SendRivalInfo(c);
                SendRivalInfo(client);
            }
        }
    }

    public static void Send(SClient cl, Message msg) {
        try {
            cl.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Sends the rival name to the user
    private static void SendRivalInfo(SClient cl) {
        Message msg = new Message(Message_Type.RivalFound);
        msg.content = cl.rival.name;
        Send(cl, msg);
    }

    //Sends the location of paddle to the rival
    public static void PaddleUp(SClient cl, Message msg) {
        Message up = msg;
        Send(cl.rival, up);
    }

    public static void PaddleDown(SClient cl, Message msg) {
        Message down = msg;
        Send(cl.rival, down);
    }

    public static void PaddleStopped(SClient cl, Message msg) {
        Message up = msg;
        Send(cl.rival, up);
    }

    static void ScoreChanged(SClient cl, Message msg) {
        Message score = msg;
        Send(cl.rival, score);
    }

    public static void ExitServer(SClient cl) throws IOException {
        cl.soket.close();
        cl.sOutput.flush();
        cl.sOutput.close();
        cl.sInput.close();
    }

    public static class ServerThread extends Thread {

        @Override
        public void run() {
            while (!Server.socketServer.isClosed()) {
                try {
                    System.out.println("Waiting..");

                    Socket clientSocket = Server.socketServer.accept();
                    SClient clientt = new SClient(clientSocket, Server.CId);

                    Server.CId++;
                    Server.clientList.add(clientt);
                    System.out.println(Server.CId + " came.");

                    clientt.listenThread.start();

                } catch (IOException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
