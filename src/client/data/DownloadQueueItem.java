package client.data;

import model.ClientDetails;

public class DownloadQueueItem {
    private String filename;
    private int retryCount;
    private ClientDetails lastRetryClient;

    public DownloadQueueItem(String fname) {
        this.filename = fname;
        this.retryCount = 0;
        this.lastRetryClient = null;
    }

    public DownloadQueueItem(String fname, int retryCount, ClientDetails lastRetryClient) {
        this.filename = fname;
        this.retryCount = retryCount;
        this.lastRetryClient = lastRetryClient;
    }

    public String getFilename() {
        return filename;
    }

    public int getRetryCount() {
        return retryCount;
    }
}
