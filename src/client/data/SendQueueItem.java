package client.data;

import model.ClientDetails;

public class SendQueueItem extends ClientDetails {
    String filename;
    int bandwidth;
    public SendQueueItem(int bandwidth, String s, int i, String clientId, String filename) {
        super(s, i, clientId);
        this.bandwidth = bandwidth;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}
