package client;

import client.threads.PingServerThread;

public class Client {
    // register ping thread
    public static void main(String[] args) {
        new PingServerThread();
        String[] files = FileHandler.getFileNames("C:\\Users\\sk111\\IdeaProjects\\xFS\\.idea");
        for(String file : files) {
            System.out.println(file);
        }
    }
}
