package client.data;

import model.ClientDetails;
import java.util.*;

public class DownloadQueueItem {
    private String filename;
    private int retryCount;
    private Set<ClientDetails> lastRetryClient;


    public DownloadQueueItem(String fname) {
        this.filename = fname;
        this.retryCount = 0;
        this.lastRetryClient =new HashSet<ClientDetails>();
    }

    public DownloadQueueItem(String fname, int retryCount, Set<ClientDetails> lastRetryClient) {
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

    public Set<ClientDetails> getLastRetryClient(){ return lastRetryClient;}
}
