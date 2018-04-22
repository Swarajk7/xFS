package client.threads;

import client.FileHandler;
import common.ConfigManager;
import common.IFileInformationServer;
import common.Utility;
import model.ClientDetails;

import java.io.IOException;
import java.rmi.Naming;

public class PingServerThread implements Runnable {
    private ConfigManager configManager;
    private ClientDetails clientDetails;
    private String filepath;
    public PingServerThread(int port, String filepath) throws IOException {
        configManager = ConfigManager.create();
        clientDetails = new ClientDetails(Utility.getIP(), port);
        this.filepath = filepath;
        new Thread(this, "Client Side Thread for Ping Server!!").start();
    }

    @Override
    public void run() {
        /*
        1. On start update the server about list of files.
        2. Then check continuously ping the server.
        3. If ping fails, set shouldUpdateServerWithListOfFiles to true.
        4. Update the server again after ping is successful.
         */
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
                    String[] files = FileHandler.getFileNames(filepath);
                    fileInformation.updateList(clientDetails, files);
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
