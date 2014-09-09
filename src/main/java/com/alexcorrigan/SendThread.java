package com.alexcorrigan;

import java.io.PrintWriter;

import static com.alexcorrigan.ConnectionReference.CLIENT;

public class SendThread extends Thread {

    private final PrintWriter writer;
    private final ConnectionReference connectionReference;
    private final MessageConductor messageConductor;

    public SendThread(PrintWriter writer, ConnectionReference connectionReference, MessageConductor messageConductor) {
        this.writer = writer;
        this.connectionReference = connectionReference;
        this.messageConductor = messageConductor;
    }

    @Override
    public void run() {
        while (true) {

            String outboundMessage;
            if (connectionReference.equals(CLIENT)) {
                outboundMessage = messageConductor.getNextClientOutboundMessage();
            } else {
                outboundMessage = messageConductor.getNextHostOutboundMessage();
            }

            if (outboundMessage != null) {
                writer.write(outboundMessage);
                writer.flush();
                System.out.println(connectionReference + " -- SENT: " + outboundMessage);
            }
        }
    }

}
