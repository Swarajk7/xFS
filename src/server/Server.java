package server;

import common.ConfigManager;
import common.Utility;
import common.IFileInformationServer;
import server.rmi.FileInformationServer;
import server.threads.RemoveDisConnectedClientsThread;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private static Registry registry;

    public static void main(String[] args) {
        try {
            startRMIServer(Utility.parseAndGetPortNumber(args));
            new RemoveDisConnectedClientsThread();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static void startRMIServer(int port) throws IOException {
        // start rmiregistry
        registry = LocateRegistry.createRegistry(port);

        // start interserver RMI
        IFileInformationServer stub = new FileInformationServer();
        Naming.rebind(Utility.getRMIEndpoint(Utility.getIP(), port, ConfigManager.create().getValue(ConfigManager.SERVER_BINDING_NAME)), stub);
    }
}
