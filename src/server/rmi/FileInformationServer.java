package server.rmi;

import common.IFileInformationServer;
import model.ClientDetails;
import server.data.ClientHeartbeat;
import server.data.FileClientMapper;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

public class FileInformationServer extends UnicastRemoteObject implements IFileInformationServer {
    public FileInformationServer() throws RemoteException {
        super();
    }

    @Override
    public void updateList(ClientDetails clientDetails, String[] fileList) throws RemoteException {
        System.out.println("UpdateList() called." + clientDetails + ":" + fileList.length);
        try {
            FileClientMapper.addClients(fileList, clientDetails);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public List<ClientDetails> find(String filename) throws RemoteException {
        System.out.println("find() called, filename: " + filename);
        try {
            return Arrays.asList(FileClientMapper.getListOfClients(filename));
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void ping(ClientDetails clientDetails) throws RemoteException {
        //System.out.println("Ping()");
        try {
            ClientHeartbeat.addHeartBeat(clientDetails);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }
}
