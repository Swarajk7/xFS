package client.threads;

import client.FileDownloader;

import java.io.IOException;

public class DownloadHostThread implements Runnable {
    private int port;
    private String filepath;

    public DownloadHostThread(int port, String filepath) {
        this.port = port;
        this.filepath = filepath;
        new Thread(this, "DownloadHostThread:port:" + port).start();
    }

    @Override
    public void run() {
        FileDownloader fileDownloader = new FileDownloader();
        try {
            fileDownloader.send(port, filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
