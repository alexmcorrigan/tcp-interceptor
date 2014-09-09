package com.alexcorrigan;

import java.util.LinkedList;
import java.util.Queue;

public class MessageConductor {

    private final String messageFilter;
    private final int numberOfMessagesToDrop;
    private volatile Queue<String> clientOutboundMessageQueue = new LinkedList<String>();
    private volatile Queue<String> hostOutboundMessageQueue = new LinkedList<String>();
    private int droppedMessages = 0;

    public MessageConductor(String messageFilter, int numberOfMessagesToDrop) {
        this.messageFilter = messageFilter;
        this.numberOfMessagesToDrop = numberOfMessagesToDrop;
    }

    public String getNextClientOutboundMessage() {
        return clientOutboundMessageQueue.poll();
    }

    public String getNextHostOutboundMessage() {
        return hostOutboundMessageQueue.poll();
    }

    public void sendToHostOutboundQueue(String message) {
        hostOutboundMessageQueue.add(message);
        System.out.println("CLIENT -> HOST: " + message);
    }

    public void sendToClientOutboundQueue(String message) {
        if (shouldDropMessage(message)) {
            drop(message);
        } else {
            send(message);
        }
    }

    private boolean shouldDropMessage(String message) {
        return numberOfMessagesToDrop == -1 || (droppedMessages < numberOfMessagesToDrop && message.contains(messageFilter));
    }

    private void drop(String message) {
        droppedMessages ++;
        System.out.println("DROPPED -- FROM HOST: " + message);
    }

    private void send(String message) {
        clientOutboundMessageQueue.add(message);
        System.out.println("HOST -> CLIENT: " + message);
    }

}
