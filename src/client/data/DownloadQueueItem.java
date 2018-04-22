package client.data;

import model.ClientDetails;

public class DownloadQueueItem extends ClientDetails {
    String filename;
    public DownloadQueueItem(String s, int i, String filename) {
        super(s, i);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}
