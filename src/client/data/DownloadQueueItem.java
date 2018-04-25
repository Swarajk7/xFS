package client.data;

import model.ClientDetails;
import java.util.*;

public class DownloadQueueItem {
    private String filename;
    private int retryCount;
    private ArrayList<ClientDetails> lastRetryClient;

    public DownloadQueueItem(String fname) {
        this.filename = fname;
        this.retryCount = 0;
        this.lastRetryClient =new ArrayList<ClientDetails>();
    }

    public DownloadQueueItem(String fname, int retryCount, ArrayList<ClientDetails> lastRetryClient) {
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

    public ArrayList<ClientDetails> getLastRetryClient(){ return lastRetryClient;}
}
