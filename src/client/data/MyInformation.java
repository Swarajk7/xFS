package client.data;

import model.ClientDetails;

public class MyInformation {
    private static ClientDetails clientDetails;
    // Speed in Mbps
    private static int bandWidth = 50 + (int)(Math.random() * 20);

    public static void setMyInformation(ClientDetails clientDetails1) {
        clientDetails = clientDetails1;
    }

    public static ClientDetails getMyInformation() {
        return clientDetails;
    }
    public static int getBandwidth() {
        return bandWidth;
    }
}
