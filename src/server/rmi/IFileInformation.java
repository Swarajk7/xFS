package server.rmi;

import model.ClientDetails;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IFileInformation  extends Remote {
    void updateList(ClientDetails clientDetails, String[] fileList) throws RemoteException;

    List<ClientDetails> find(String filename) throws RemoteException;

    void Ping(ClientDetails clientDetails) throws RemoteException;
}
