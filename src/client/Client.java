package client;


import client.threads.PingServerThread;
import client.threads.ReceiverHostThread;
import common.Utility;
import model.ClientDetails;

import java.io.IOException;

public class Client {
    // register ping thread
    public static void main(String[] args) {
        try {
            //new PingServerThread(Utility.parseAndGetPortNumber(args), "C:\\Users\\sk111\\IdeaProjects\\xFS\\src\\client");
            if(Utility.parseAndGetPortNumber(args) == 6005) {
                new ReceiverHostThread(new ClientDetails("10.0.0.210", 6006), "C:\\Users\\sk111\\Documents\\Random\\code\\input.txt");
            } else {
                FileDownloader fileDownloader = new FileDownloader();
                fileDownloader.download(6006, "C:\\Users\\sk111\\Documents\\Random\\code\\inpu2t.txt");
            }
        } catch (IOException e) {
            System.out.println("Check if Client is able to access the config file.");
        }
    }
}
