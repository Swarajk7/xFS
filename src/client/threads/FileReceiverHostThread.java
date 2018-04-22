package client.threads;

import client.FileDownloader;
import client.FileHandler;
import model.ClientDetails;

import java.io.IOException;

public class FileReceiverHostThread implements Runnable {
    private int port;
    private String filename,ip;

    public FileReceiverHostThread(ClientDetails receiverClientDetails, String filename) {
        this.port = receiverClientDetails.getPort();
        this.filename = filename;
        this.ip = receiverClientDetails.getIp();
        new Thread(this, "DownloadHostThread:port:" + port).start();
    }

    @Override
    public void run() {
        FileDownloader fileDownloader = new FileDownloader();
        try {
            fileDownloader.download(port, FileHandler.getFilePathForFileName(filename));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
