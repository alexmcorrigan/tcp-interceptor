package com.alexcorrigan;

import java.io.BufferedReader;
import java.io.IOException;

import static com.alexcorrigan.ConnectionReference.CLIENT;

public class ReceiveThread extends Thread {

    private final BufferedReader reader;
    private final ConnectionReference connectionReference;
    private MessageConductor messageConductor;

    public ReceiveThread(BufferedReader reader, ConnectionReference connectionReference, MessageConductor messageConductor) {
        this.reader = reader;
        this.connectionReference = connectionReference;
        this.messageConductor = messageConductor;
    }

    @Override
    public void run() {
        StringBuffer messageBuffer = new StringBuffer();
        int c;
        boolean foundEnd = false;
        try {
            while ( (c = reader.read()) != -1) {
                messageBuffer.append((char) c);

                if (MessageBufferUtils.charIndicatesFixField(messageBuffer, c)) {
                    foundEnd = MessageBufferUtils.haveReachedEndOfMessage(messageBuffer);
                }
                if (c == 1 && foundEnd) {
                    String message = messageBuffer.toString();
                    System.out.println(connectionReference + " -- RECEIVED: " + message);

                    if (connectionReference.equals(CLIENT)) {
                        messageConductor.sendToHostOutboundQueue(message);
                    } else {
                        messageConductor.sendToClientOutboundQueue(message);
                    }

                    messageBuffer = new StringBuffer();
                    foundEnd = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
