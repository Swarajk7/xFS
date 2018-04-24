package client;


import client.data.SendQueue;
import client.rmi.FileDownloaderClient;
import client.threads.FileReceiverHostThread;
import client.threads.FileSenderThread;
import client.threads.PingServerThread;
import common.ConfigManager;
import common.IFileDownloaderClient;
import common.Utility;
import model.ClientDetails;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private static Registry registry;

    // register ping thread
    public static void main(String[] args) {
        try {
            setBasePath(Utility.parseAndGetClientId(args));
            int port = Utility.parseAndGetPortNumber(args);
            new PingServerThread(port);
            for (int i = 0; i < SendQueue.getMaxSupportedConcurrentSend(); i++)
                new FileSenderThread(i);
            startRMIServer(port);
            /*
            Build client logic here.
            1. DownloadFile(filename) -
                -- need to build the logic here.
                -- 1. FindFile(filename) - by calling Server's RMI endpoint.
                -- 2. Ask Each peer for Load.
                -- 3. Chose a peer.
                -- 4. Download the file from that peer.
                -- 5. Verify Checksum.
                -- 5. Detect failure.
             */
            /*
             Code below is just for testing the download.
             */
            if (port == 6005) {
                String endPoint = Utility.getRMIEndpoint("10.0.0.210", 6006,
                        "client");
                IFileDownloaderClient stub = (IFileDownloaderClient) Naming.lookup(endPoint);
                new FileReceiverHostThread(new ClientDetails("10.0.0.210", 8000), "a.txt");
                stub.requestFileSend(new ClientDetails("10.0.0.210", 8000), "a.txt");
            }
        } catch (IOException e) {
            System.out.println("Check if Client is able to access the config file.");
        } catch (NotBoundException e) {
            System.out.println("Not able to do RMI call.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void startRMIServer(int port) throws IOException {
        // start rmiregistry
        registry = LocateRegistry.createRegistry(port);

        // start interserver RMI
        IFileDownloaderClient stub = new FileDownloaderClient();
        Naming.rebind(Utility.getRMIEndpoint(Utility.getIP(), port, ConfigManager.create().getValue(ConfigManager.CLIENT_BINDING_NAME)), stub);
    }

    private static void setBasePath(String clientid) throws IOException {
        ConfigManager configManager = ConfigManager.create();
        String filepath = configManager.getValue(ConfigManager.BASE_PATH) + clientid;
        FileHandler.setBasepath(filepath);
    }
}
