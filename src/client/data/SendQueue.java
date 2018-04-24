package client.data;

import common.ConfigManager;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SendQueue {
    private static Queue<SendQueueItem> sendQueue;
    private static int maxConcurrentSend;
    private static AtomicInteger onGoingSends;
    private int waitTime = 0;

    static {
        try {
            ConfigManager configManager = ConfigManager.create();
            maxConcurrentSend = configManager.getIntegerValue(ConfigManager.NUMBER_OF_UPLOAD_THREADS);
            sendQueue = new ConcurrentLinkedQueue<>();
            onGoingSends = new AtomicInteger();
        } catch (IOException exception) {
            System.out.println("Not able to read config file");
            System.exit(1);
        }

    }

    public static int getMaxSupportedConcurrentSend() {
        return maxConcurrentSend;
    }

    public static int getOnGoingSends() {
        // This will be our load.
        return onGoingSends.get() + sendQueue.size();
    }

    public static void addSendRequestToQueue(SendQueueItem item) {
        sendQueue.add(item);
    }

    public static SendQueueItem getItemToSend() {
        SendQueueItem item = sendQueue.poll();
        onGoingSends.incrementAndGet();
        return item;
    }

    public static void finishSendingItem() {
        onGoingSends.decrementAndGet();
    }
}
