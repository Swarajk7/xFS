package server;

import common.ConfigManager;
import common.Utility;
import server.rmi.FileInformation;
import server.rmi.IFileInformation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private static Registry registry;

    public static void main(String[] args) {
        try {
            startRMIServer(Utility.parseAndGetPortNumber(args));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static void startRMIServer(int port) throws IOException {
        // start rmiregistry
        registry = LocateRegistry.createRegistry(port);

        // start interserver RMI
        IFileInformation stub = new FileInformation();
        Naming.rebind(Utility.getRMIEndpoint(Utility.getIP(), port, ConfigManager.create().getValue(ConfigManager.SERVER_BINDING_NAME)), stub);
    }
}
