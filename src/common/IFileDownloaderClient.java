package common;

import model.ClientDetails;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFileDownloaderClient extends Remote {
    // here send the receiver's socket ip and port number and the needed file name.
    void requestFileSend(int bandwidth, ClientDetails clientDetails, String filename) throws RemoteException;
    long getLoad(int bandwidth, ClientDetails receiver, String filename) throws RemoteException;
    String getCheckSum(String filename) throws RemoteException;
}
