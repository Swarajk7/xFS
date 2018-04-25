package client.data;

import model.ClientDetails;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SendQueueItem extends ClientDetails {
    String filename;
    int bandwidth;
    private LocalDateTime createdDateTime;
    private long waittime;

    public SendQueueItem(int bandwidth, String s, int i, String clientId, String filename, long waittime) {
        super(s, i, clientId);
        this.bandwidth = bandwidth;
        this.filename = filename;
        this.createdDateTime = LocalDateTime.now();
        this.waittime = waittime;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isExpired() {
        LocalDateTime tempDateTime = LocalDateTime.now();
        long seconds = createdDateTime.until(tempDateTime, ChronoUnit.SECONDS);
        return seconds > waittime;
    }
}
