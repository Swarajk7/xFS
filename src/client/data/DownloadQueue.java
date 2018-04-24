package client.data;

import common.ConfigManager;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadQueue {
    private static Queue<DownloadQueueItem> downloadQueue;
    private static int maxConcurrentDownload;

    static {
        try {
            ConfigManager configManager = ConfigManager.create();
            maxConcurrentDownload = configManager.getIntegerValue(ConfigManager.NUMBER_OF_UPLOAD_THREADS);
            downloadQueue = new ConcurrentLinkedQueue<>();
        } catch (IOException exception) {
            System.out.println("Not able to read config file");
            System.exit(1);
        }
    }

    public static int getMaxSupportedConcurrentDownload() {
        return maxConcurrentDownload;
    }

    public static void addDownloadRequestToQueue(DownloadQueueItem item) {
        downloadQueue.add(item);
    }

    public static DownloadQueueItem getItemToDownload() {
        return downloadQueue.poll();
    }
}
