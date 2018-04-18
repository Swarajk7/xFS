package common;

import model.ClientDetails;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IFileInformationServer extends Remote {
    void updateList(ClientDetails clientDetails, String[] fileList) throws RemoteException;

    List<ClientDetails> find(String filename) throws RemoteException;

    void ping(ClientDetails clientDetails) throws RemoteException;
}
