package server.threads;

import common.ConfigManager;
import model.ClientDetails;
import server.data.ClientHeartbeat;
import server.data.FileClientMapper;

import java.io.IOException;
import java.util.ArrayList;

public class RemoveDisConnectedClientsThread implements Runnable {
    private int threshold;

    public RemoveDisConnectedClientsThread() throws IOException {
        threshold = ConfigManager.create().getIntegerValue(ConfigManager.THRESHOLD_TIMEOUT_FOR_CLIENT);
        new Thread(this, "RemoveDisConnectedClientsThread()").start();
    }

    @Override
    public void run() {
        /*
        1. Check for all client's last hearbeat status.
        2. If it is more than 10 seconds than the current time, delete the client from FileMapping.
         */
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
