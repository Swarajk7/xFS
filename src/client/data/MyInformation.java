package client.data;

import model.ClientDetails;

public class MyInformation {
    private static ClientDetails clientDetails;

    public static void setMyInformation(ClientDetails clientDetails1) {
        clientDetails = clientDetails1;
    }

    public static ClientDetails getMyInformation() {
        return clientDetails;
    }
}
