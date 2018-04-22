package client.rmi;

import client.FileDownloader;
import client.FileHandler;
import client.data.DownloadQueueItem;
import client.data.DownloadRequestQueue;
import common.IFileDownloaderClient;
import model.ClientDetails;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileDownloaderClient extends UnicastRemoteObject implements IFileDownloaderClient {
    public FileDownloaderClient() throws RemoteException {
        super();
    }

    @Override
    public void requestFileSend(ClientDetails clientDetails, String filename) throws RemoteException {
        try {
            DownloadRequestQueue.addDownloadRequestToQueue(new DownloadQueueItem(clientDetails.getIp(), clientDetails.getPort(), filename));
            // Here we can send the checkSum.
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RemoteException(ex.getMessage());
        }
    }

    public int getLoad() throws RemoteException {
        try {
            return DownloadRequestQueue.getOnGoingDownloads();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RemoteException(ex.getMessage());
        }
    }
}
