package client.threads;

import common.ConfigManager;
import common.IFileInformationServer;
import common.Utility;
import model.ClientDetails;

import java.io.IOException;
import java.rmi.Naming;

public class PingServerThread implements Runnable {
    private ConfigManager configManager;
    private ClientDetails clientDetails;
    public PingServerThread(int port) throws IOException {
        configManager = ConfigManager.create();
        clientDetails = new ClientDetails();
        clientDetails.setIp(Utility.getIP());
        clientDetails.setPort(port);
        new Thread(this, "Client Side Thread for Ping Server!!").start();
    }

    @Override
    public void run() {
        boolean shouldUpdateServerWithListOfFiles = true;
        while (true) {
            try {
                // ping server
                IFileInformationServer fileInformation = (IFileInformationServer) Naming.lookup(
                        Utility.getRMIEndpoint(configManager.getValue(ConfigManager.SERVER_IP_ADDRESS),
                                configManager.getIntegerValue(ConfigManager.SERVER_PORT_NUMBER),
                                configManager.getValue(ConfigManager.SERVER_BINDING_NAME)));
                fileInformation.ping(clientDetails);
                System.out.println("Successfully Pinged Server.");
                // if ping is sucessful, server is up. :)
                if (shouldUpdateServerWithListOfFiles) {
                    // tell the server about your files by calling appropriate endpoint.
                    // if sucessful set shouldUpdateServerWithListOfFiles to false.
                    shouldUpdateServerWithListOfFiles = false;
                    System.out.println("Updated Server With List of Files.");
                }
            } catch (Exception ex) {
                System.out.println("Server is Down. " + ex.getMessage());
                shouldUpdateServerWithListOfFiles = true;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
