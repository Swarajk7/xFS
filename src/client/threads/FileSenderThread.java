package client.threads;

import client.FileDownloader;
import client.FileHandler;
import client.data.SendQueueItem;
import client.data.SendQueue;

import java.io.IOException;

public class FileSenderThread implements Runnable {
    public FileSenderThread(int countid) {
        String name = "FileSenderThread" + countid;
        new Thread(this, name).start();
    }

    @Override
    public void run() {
        /*
        1. Poll the available queues.
        2. Pick one file if available to send.
        3. Send the file.
        4. Repeat
         */
        while (true) {
            SendQueueItem item = SendQueue.getItemToSend();
            if (item == null) {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                    System.out.println("Thread Name:" + this.toString() + " " + ex.getMessage());
                }
            } else {
                /*
                What if download fails?
                What to do?
                 */
                FileDownloader downloader = new FileDownloader();
                try {
                    String path = FileHandler.getFilePathForFileName(item.getFilename());
                    downloader.send(item.getIp(), item.getPort(), path);
                } catch (IOException e) {
                    System.out.println("Thread Name:" + this.toString() + " " + e.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                SendQueue.finishSendingItem();
            }
        }
    }
}
