package com.alexcorrigan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionThread extends Thread {

    private MessageConductor messageConductor;
    private PrintWriter writer;
    private BufferedReader reader;
    private ConnectionReference connectionReference;

    public static ConnectionThread createHost(MessageConductor messageConductor, int listenPort) throws IOException {
        ServerSocket serverSocket = new ServerSocket(listenPort);
        return new ConnectionThread(ConnectionReference.HOST, serverSocket.accept(), messageConductor);
    }

    public static ConnectionThread createClient(MessageConductor messageConductor, String connectHost, int connectPort) throws IOException {
        return new ConnectionThread(ConnectionReference.CLIENT, new Socket(connectHost, connectPort), messageConductor);
    }

    private ConnectionThread(ConnectionReference connectionReference, Socket socket, MessageConductor messageConductor) throws IOException {
        this.messageConductor = messageConductor;
        this.connectionReference = connectionReference;
        writer = new PrintWriter(socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            Thread receiveThread = new ReceiveThread(reader, connectionReference, messageConductor);
            Thread sendThread = new SendThread(writer, connectionReference, messageConductor);
            
            receiveThread.start();
            sendThread.start();
            
            receiveThread.join();
            sendThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

}
