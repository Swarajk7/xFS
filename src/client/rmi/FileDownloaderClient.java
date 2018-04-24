package client.rmi;

import client.data.SendQueueItem;
import client.data.SendQueue;
import common.IFileDownloaderClient;
import model.ClientDetails;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileDownloaderClient extends UnicastRemoteObject implements IFileDownloaderClient {
    public FileDownloaderClient() throws RemoteException {
        super();
    }

    @Override
    public void requestFileSend(ClientDetails clientDetails, String filename) throws RemoteException {
        try {
            SendQueue.addSendRequestToQueue(new SendQueueItem(clientDetails.getIp(), clientDetails.getPort(), filename));
            // Here we can send the checkSum.
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RemoteException(ex.getMessage());
        }
    }

    public int getLoad() throws RemoteException {
        try {
            return SendQueue.getOnGoingSends();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RemoteException(ex.getMessage());
        }
    }
}
