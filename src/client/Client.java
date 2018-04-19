package client;

import client.threads.PingServerThread;
import common.Utility;

import java.io.IOException;

public class Client {
    // register ping thread
    public static void main(String[] args) {
        try {
            new PingServerThread(Utility.parseAndGetPortNumber(args), "C:\\Users\\sk111\\IdeaProjects\\xFS\\src\\client");
        } catch (IOException e) {
            System.out.println("Check if Client is able to access the config file.");
        }
    }
}
