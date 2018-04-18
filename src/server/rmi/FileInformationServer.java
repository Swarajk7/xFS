package server.rmi;

import common.IFileInformationServer;
import model.ClientDetails;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class FileInformationServer extends UnicastRemoteObject implements IFileInformationServer {
    public FileInformationServer() throws RemoteException {
        super();
    }

    @Override
    public void updateList(ClientDetails clientDetails, String[] fileList) throws RemoteException {
        System.out.println("UpdateList() called.");
    }

    @Override
    public List<ClientDetails> find(String filename) throws RemoteException {
        return null;
    }

    @Override
    public void ping(ClientDetails clientDetails) throws RemoteException {
        System.out.println("Ping()");
    }
}
