package client.rmi;

import client.FileHandler;
import client.data.MyInformation;
import client.data.SendQueueItem;
import client.data.SendQueue;
import common.IFileDownloaderClient;
import model.ClientDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileDownloaderClient extends UnicastRemoteObject implements IFileDownloaderClient {
    public FileDownloaderClient() throws RemoteException {
        super();
    }

    @Override
    public void requestFileSend(int bandwidth, ClientDetails clientDetails, String filename) throws RemoteException {
        try {
            int eta = 15;

            SendQueue.addSendRequestToQueue(new SendQueueItem(bandwidth,clientDetails.getIp(), clientDetails.getPort(),clientDetails.getClientId(), filename));
            // Here we can send the checkSum.
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RemoteException(ex.getMessage());
        }
    }


    public String getCheckSum(String filename) throws RemoteException {
        try {
            return FileHandler.calculateCheckSum(filename);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RemoteException(ex.getMessage());
        }
    }


    public long getLoad(int bandwidth, ClientDetails receiver, String filename) throws RemoteException {
        try {
            return SendQueue.getExpectedDownloadTime(bandwidth, receiver, filename);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RemoteException(ex.getMessage());
        }
    }
}
