package server.data;

import common.ConfigManager;
import model.ClientDetails;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHeartbeat {
    private static ConcurrentHashMap<ClientDetails, LocalDateTime> clientHeartBeat = new ConcurrentHashMap<>();
    private static long thresholdTimeOutForClient;

    static {
        try {
            thresholdTimeOutForClient = ConfigManager.create().getIntegerValue(ConfigManager.THRESHOLD_TIMEOUT_FOR_CLIENT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void addHeartBeat(ClientDetails clientDetails) {
        clientHeartBeat.put(clientDetails, LocalDateTime.now());
    }

    public static void removeClient(ClientDetails clientDetails) {
        if (clientHeartBeat.containsKey(clientDetails)) {
            clientHeartBeat.remove(clientDetails);
        }
    }

    public static boolean isClientDisconnected(ClientDetails clientDetails) {
        if (!clientHeartBeat.containsKey(clientDetails)) return true;
        LocalDateTime heartBeatTime = clientHeartBeat.get(clientDetails);
        LocalDateTime tempDateTime = LocalDateTime.now();
        long seconds = heartBeatTime.until(tempDateTime, ChronoUnit.SECONDS);
        return seconds > thresholdTimeOutForClient;
    }

    public static ArrayList<ClientDetails> getClientList() {
        ArrayList<ClientDetails> clientDetailsArrayList = new ArrayList<>();
        clientDetailsArrayList.addAll(clientHeartBeat.keySet());
        return clientDetailsArrayList;
    }
}
