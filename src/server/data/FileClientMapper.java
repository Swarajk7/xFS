package server.data;

import model.ClientDetails;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileClientMapper {
    private static HashMap<String, Set<ClientDetails>> fileToClientDetailsMap = new HashMap<>();

    private static void addClient(String filename, ClientDetails clientDetails) {
        if (fileToClientDetailsMap.containsKey(filename)) {
            fileToClientDetailsMap.get(filename).add(clientDetails);
        } else {
            // Assuming that maximum number of clients for this system will be 100
            Set<ClientDetails> set = (Set) ConcurrentHashMap.newKeySet(100);
            set.add(clientDetails);
            fileToClientDetailsMap.put(filename, set);
        }
    }

    public static void addClients(String[] filenames, ClientDetails clientDetails) {
        for (String filename : filenames) {
            addClient(filename, clientDetails);
        }
    }

    public static void removeClient(ClientDetails clientDetails) {
        Iterator it = fileToClientDetailsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            Set<ClientDetails> set = (Set) pair.getValue();
            if (set.contains(clientDetails)) {
                if (set.size() == 1)
                    it.remove(); // avoids a ConcurrentModificationException
                else
                    fileToClientDetailsMap.get(pair.getKey()).remove(clientDetails);
            }
        }
    }

    public static ClientDetails[] getListOfClients(String filename) {
        Set<ClientDetails> set = (Set) fileToClientDetailsMap.get(filename);
        return set.toArray(new ClientDetails[0]);
    }
}
