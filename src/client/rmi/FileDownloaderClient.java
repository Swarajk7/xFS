package client.rmi;

import common.IFileDownloaderClient;
import model.ClientDetails;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileDownloaderClient extends UnicastRemoteObject implements IFileDownloaderClient {
    protected FileDownloaderClient() throws RemoteException {
        super();
    }

    @Override
    public int requestDownloadFile(ClientDetails clientDetails) throws RemoteException {
        // need to find a way to start thread.
        return 6006;
    }
}
