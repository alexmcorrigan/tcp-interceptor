package com.alexcorrigan;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class App {

    public static final String APP_PROPERTIES_PATH = "./config/tcpTweak.properties";

    public static void main(String[] args) throws IOException {
        Properties appProperties = new Properties();
        appProperties.load(new FileInputStream(APP_PROPERTIES_PATH));
        new App().run(appProperties);
    }

    private void run(Properties appProperties) throws IOException {

        MessageConductor messageConductor = new MessageConductor(
                appProperties.getProperty("messageFilter"),
                Integer.valueOf(appProperties.getProperty("numberOfMessagesToDrop")));

        Thread clientThread = buildClientThread(appProperties, messageConductor);
        Thread hostThread = buildHostThread(appProperties, messageConductor);

        clientThread.start();
        hostThread.start();

    }

    private ConnectionThread buildHostThread(Properties appProperties, MessageConductor messageConductor) throws IOException {
        return ConnectionThread.createHost(messageConductor, Integer.valueOf(appProperties.getProperty("listenPort")));
    }

    private ConnectionThread buildClientThread(Properties appProperties, MessageConductor messageConductor) throws IOException {
        return ConnectionThread.createClient(
                messageConductor,
                appProperties.getProperty("connectHost"),
                Integer.valueOf(appProperties.getProperty("connectPort")));
    }

}
