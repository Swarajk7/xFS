package client.threads;

import client.FileDownloader;
import model.ClientDetails;

import java.io.IOException;

public class FileReceiverHostThread implements Runnable {
    private int port;
    private String filepath,ip;

    public FileReceiverHostThread(ClientDetails receiverClientDetails, String filepath) {
        this.port = receiverClientDetails.getPort();
        this.filepath = filepath;
        this.ip = receiverClientDetails.getIp();
        new Thread(this, "DownloadHostThread:port:" + port).start();
    }

    @Override
    public void run() {
        FileDownloader fileDownloader = new FileDownloader();
        try {
            fileDownloader.download(port, filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
