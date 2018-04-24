package client.threads;

import client.FileDownloader;
import client.FileHandler;
import model.ClientDetails;

import java.io.IOException;

public class FileReceiverSocketThread extends Thread implements Runnable {
    private int port;
    private String filename, ip;

    public FileReceiverSocketThread(ClientDetails receiverClientDetails, String filename) {
        this.port = receiverClientDetails.getPort();
        this.filename = filename;
        this.ip = receiverClientDetails.getIp();
    }

    @Override
    public void run() {
        FileDownloader fileDownloader = new FileDownloader();
        try {
            fileDownloader.download(port, FileHandler.getFilePathForFileName(filename));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

