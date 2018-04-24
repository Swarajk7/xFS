package client.data;

import model.ClientDetails;

public class SendQueueItem extends ClientDetails {
    String filename;
    public SendQueueItem(String s, int i, String filename) {
        super(s, i);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}
