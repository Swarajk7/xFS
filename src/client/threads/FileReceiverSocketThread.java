package client.threads;

import client.FileDownloader;
import client.FileHandler;
import model.ClientDetails;

import java.io.IOException;

public class FileReceiverSocketThread extends Thread implements Runnable {
    private int port, socketTimeOutInSeconds;
    private String filename, ip;

    public FileReceiverSocketThread(ClientDetails receiverClientDetails, String filename, int socketTimeOut) {
        this.port = receiverClientDetails.getPort();
        this.filename = filename;
        this.ip = receiverClientDetails.getIp();
        this.socketTimeOutInSeconds = socketTimeOut;
    }

    @Override
    public void run() {
        FileDownloader fileDownloader = new FileDownloader();
        try {
            fileDownloader.download(port, FileHandler.getFilePathForFileName(filename), socketTimeOutInSeconds);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}

