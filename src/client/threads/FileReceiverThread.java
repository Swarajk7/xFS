package client.threads;

import client.FileDownloader;
import client.FileHandler;
import client.data.DownloadQueue;
import client.data.DownloadQueueItem;
import client.data.MyInformation;
import common.ConfigManager;
import common.IFileDownloaderClient;
import common.IFileInformationServer;
import common.Utility;
import model.ClientDetails;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.List;

public class FileReceiverThread implements Runnable {
    private int threadid,port;

    public FileReceiverThread(int i,int port) {
        this.threadid = i;
        this.port = port;
        new Thread(this, "DownloadHostThread:port:" + threadid).start();
    }

    @Override
    public void run() {

        while (true) {
            try {
                /*
                1. DownloadFile(filename) -
                -- need to build the logic here.
                -- 1. FindFile(filename) - by calling Server's RMI endpoint.
                -- 2. Ask Each peer for Load.
                -- 3. Chose a peer.
                -- 4. Download the file from that peer.
                -- 5. Verify Checksum.
                -- 5. Detect failure.
                 */
                DownloadQueueItem downloadQueueItem = DownloadQueue.getItemToDownload();
                if (downloadQueueItem == null) {
                    Thread.sleep(1000);
                } else {
                    ConfigManager configManager = ConfigManager.create();
                    String endPoint = Utility.getRMIEndpoint(configManager.getValue(ConfigManager.SERVER_IP_ADDRESS),
                            configManager.getIntegerValue(ConfigManager.SERVER_PORT_NUMBER),
                            configManager.getValue(ConfigManager.SERVER_BINDING_NAME));
                    IFileInformationServer server_stub = (IFileInformationServer) Naming.lookup(endPoint);
                    List<ClientDetails> clientList = server_stub.find(downloadQueueItem.getFilename());


                    for (ClientDetails clientDetails : clientList) {
                        String clientEndPoint = Utility.getRMIEndpoint(clientDetails.getIp(), clientDetails.getPort(),
                                configManager.getValue(ConfigManager.CLIENT_BINDING_NAME));
                        IFileDownloaderClient client_stub = (IFileDownloaderClient) Naming.lookup(clientEndPoint);
                        client_stub.getLoad();
                    }

                    //chose one client
                    ClientDetails clientDetails = new ClientDetails("a", 1);
                    String clientEndPoint = Utility.getRMIEndpoint(clientDetails.getIp(), clientDetails.getPort(),
                            configManager.getValue(ConfigManager.CLIENT_BINDING_NAME));
                    IFileDownloaderClient client_stub = (IFileDownloaderClient) Naming.lookup(clientEndPoint);

                    FileReceiverSocketThread receiverSocketThread = new FileReceiverSocketThread(new ClientDetails(Utility.getIP(), this.port),
                            downloadQueueItem.getFilename());
                    receiverSocketThread.start();
                    client_stub.requestFileSend(clientDetails, downloadQueueItem.getFilename());
                    receiverSocketThread.join();
                }
            } catch (IOException e) {
                System.out.println("Thread" + threadid + ": " + e.getMessage());
            } catch (NotBoundException e) {
                System.out.println("Thread" + threadid + ": " + e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("Thread" + threadid + ": " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Thread" + threadid + ": " + e.getMessage());
            }
        }
    }
}
