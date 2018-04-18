package client;

import client.threads.PingServerThread;
import common.Utility;

import java.io.IOException;

public class Client {
    // register ping thread
    public static void main(String[] args) {
        try {
            new PingServerThread(Utility.parseAndGetPortNumber(args));
        } catch (IOException e) {
            System.out.println("Check if Client is able to access the config file.");
        }
        String[] files = FileHandler.getFileNames("C:\\Users\\sk111\\IdeaProjects\\xFS\\.idea");
        for(String file : files) {
            System.out.println(file);
        }
    }
}
