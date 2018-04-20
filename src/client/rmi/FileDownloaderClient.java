package client.rmi;

import client.FileDownloader;
import client.FileHandler;
import common.IFileDownloaderClient;
import model.ClientDetails;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileDownloaderClient extends UnicastRemoteObject implements IFileDownloaderClient {
    protected FileDownloaderClient() throws RemoteException {
        super();
    }

    @Override
    public void requestFileSend(ClientDetails clientDetails, String filename) throws RemoteException {
        // need to find a way to start thread.
        FileDownloader fileDownloader = new FileDownloader();
        try {
            fileDownloader.send(clientDetails.getIp(), clientDetails.getPort(), FileHandler.getFilePathForFileName(filename));
        } catch (IOException e) {
            throw new RemoteException(e.getMessage());
        }
    }
}
