package client.data;

import client.FileHandler;
import common.ConfigManager;
import model.ClientDetails;

import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SendQueue {
    private static Queue<SendQueueItem> sendQueue;
    private static int maxConcurrentSend;
    private static AtomicInteger onGoingSends;
    private static int waitTime ;

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
        ClientDetails receiver  = new ClientDetails(item.getIp(),item.getPort(),item.getClientId());
        long expectedTime = getExpectedDownloadTime(item.bandwidth, receiver,  item.filename);
        waitTime += expectedTime;
    }

    public static long getFileSizeInMb(String filepath){
        File file = new File(filepath);
        long fileSizeInBytes = file.length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;

        return fileSizeInMB;
    }



    public static long getExpectedDownloadTime(int bandwidth, ClientDetails receiver, String filename){
        //considers the approx time for one of the threads to get free
        int downloadTime = waitTime/getMaxSupportedConcurrentSend();
        try{
        if(maxConcurrentSend - onGoingSends.get() > 0){
            downloadTime = 0;
        }
        int minBandwidth = Math.min(bandwidth,MyInformation.getBandwidth());

        ConfigManager configManager = ConfigManager.create();
        String filepath = FileHandler.getFilePathForFileName(filename);
        String src_dest =  MyInformation.getMyInformation().getClientId() + "-" + receiver.getClientId();
        int latency = configManager.getIntegerValue(src_dest);
        // latency is in ms
        downloadTime += latency/1000;
        downloadTime += getFileSizeInMb(filepath) / minBandwidth;
        }catch (Exception exception) {
            System.out.println("Missing Latency data. Setting to a default.");
            downloadTime = 100000000;
        }
        return downloadTime;
    }

    public static SendQueueItem getItemToSend() {
        SendQueueItem item = sendQueue.poll();
        onGoingSends.incrementAndGet();
        if (item != null) {
            ClientDetails receiver = new ClientDetails(item.getIp(), item.getPort(), item.getClientId());
            long expectedTime = getExpectedDownloadTime(item.bandwidth, receiver, item.filename);
            waitTime -= expectedTime;
        }
        return item;
    }

    public static void finishSendingItem() {
        onGoingSends.decrementAndGet();
    }
}
