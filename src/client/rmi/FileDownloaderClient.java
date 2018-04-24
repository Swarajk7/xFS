package client.rmi;

import client.FileHandler;
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
    public void requestFileSend(ClientDetails clientDetails, String filename) throws RemoteException {
        try {
            int eta = 15;

            SendQueue.addSendRequestToQueue(new SendQueueItem(clientDetails.getIp(), clientDetails.getPort(), filename));
            // Here we can send the checkSum.
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RemoteException(ex.getMessage());
        }
    }


    // Taken from https://howtodoinjava.com/core-java/io/how-to-generate-sha-or-md5-file-checksum-hash-in-java/
    public String getCheckSum(String filename) throws RemoteException
    {
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            String path = FileHandler.getFilePathForFileName(filename);
            //Get file input stream for reading the file content
            FileInputStream fis = new FileInputStream(path);

            //Create byte array to read data in chunks
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            //Read file data and update in message digest
            while ((bytesCount = fis.read(byteArray)) != -1) {
                md5Digest.update(byteArray, 0, bytesCount);
            }
            ;

            //close the stream; We don't need it now.
            fis.close();

            //Get the hash's bytes
            byte[] bytes = md5Digest.digest();

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            //return complete hash
            return sb.toString();
        }
        catch (Exception ex) {
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
