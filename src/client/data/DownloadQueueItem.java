package client.data;

public class DownloadQueueItem {
    String filename;

    public DownloadQueueItem(String fname) {
        this.filename = fname;
    }

    public String getFilename() {
        return filename;
    }
}
