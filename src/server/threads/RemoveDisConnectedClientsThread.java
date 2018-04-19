package server.threads;

import client.Client;
import common.ConfigManager;
import model.ClientDetails;
import server.data.ClientHeartbeat;
import server.data.FileClientMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class RemoveDisConnectedClientsThread implements Runnable {
    private int threshold;

    public RemoveDisConnectedClientsThread() throws IOException {
        threshold = ConfigManager.create().getIntegerValue(ConfigManager.THRESHOLD_TIMEOUT_FOR_CLIENT);
        new Thread(this, "RemoveDisConnectedClientsThread()").start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                ArrayList<ClientDetails> clientDetailsArray = ClientHeartbeat.getClientList();
                for (ClientDetails clientDetails : clientDetailsArray) {
                    if (ClientHeartbeat.isClientDisconnected(clientDetails)) {
                        System.out.println("Removing Client" + clientDetails);
                        ClientHeartbeat.removeClient(clientDetails);
                        FileClientMapper.removeClient(clientDetails);
                    }
                }
                Thread.sleep(threshold * 1000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
