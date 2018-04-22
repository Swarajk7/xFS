package client.data;

import common.ConfigManager;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadRequestQueue {
    private static Queue<DownloadQueueItem> downloadQueue;
    private static int maxConcurrentDownload;
    private static AtomicInteger onGoingDownloads;

    static {
        try {
            ConfigManager configManager = ConfigManager.create();
            maxConcurrentDownload = configManager.getIntegerValue(ConfigManager.NUMBER_OF_UPLOAD_THREADS);
            downloadQueue = new ConcurrentLinkedQueue<>();
            onGoingDownloads = new AtomicInteger();
        } catch (IOException exception) {
            System.out.println("Not able to read config file");
            System.exit(1);
        }

    }

    public static int getMaxConcurrentDownload() {
        return maxConcurrentDownload;
    }

    public static int getOnGoingDownloads() {
        // This will be our load.
        return onGoingDownloads.get() + downloadQueue.size();
    }

    public static void addDownloadRequestToQueue(DownloadQueueItem item) {
        downloadQueue.add(item);
    }

    public static DownloadQueueItem getItemToDownload() {
        DownloadQueueItem item = downloadQueue.poll();
        onGoingDownloads.incrementAndGet();
        return item;
    }

    public static void finishProcessingItem() {
        onGoingDownloads.decrementAndGet();
    }
}
