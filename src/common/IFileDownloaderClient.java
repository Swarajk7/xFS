package common;

import model.ClientDetails;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFileDownloaderClient extends Remote {
    int requestDownloadFile(ClientDetails clientDetails) throws RemoteException;
}
