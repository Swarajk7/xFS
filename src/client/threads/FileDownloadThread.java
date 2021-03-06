package client.threads;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileDownloadThread implements Runnable {
    private int threadid, port;

    public FileDownloadThread(int i, int port) {
        this.threadid = i;
        this.port = port;
        new Thread(this, "DownloadHostThread:port:" + threadid).start();
    }

    @Override
    public void run() {

        while (true) {
            DownloadQueueItem downloadQueueItem = null;
            ClientDetails chosenClientDetails = null;
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
                downloadQueueItem = DownloadQueue.getItemToDownload();

                if (downloadQueueItem == null) {
                    Thread.sleep(1000);
                } else {

                    // Get the file list from server.
                    ConfigManager configManager = ConfigManager.create();
                    String endPoint = Utility.getRMIEndpoint(configManager.getValue(ConfigManager.SERVER_IP_ADDRESS),
                            configManager.getIntegerValue(ConfigManager.SERVER_PORT_NUMBER),
                            configManager.getValue(ConfigManager.SERVER_BINDING_NAME));
                    IFileInformationServer server_stub = (IFileInformationServer) Naming.lookup(endPoint);
                    List<ClientDetails> clientList = server_stub.find(downloadQueueItem.getFilename());

                    if (clientList.size() == 0) {
                        System.out.println("File not found. File Name: " + downloadQueueItem.getFilename());
                        continue;
                    }

                    // Call each client for load.
                    /* todo: use retry count and last download attempt details to decide, whether to stop the download. */

                    long minSeenYet = Integer.MAX_VALUE;
                    ClientDetails alreadyRequested = null;
                    for (ClientDetails clientDetails : clientList) {
                        if (clientDetails.getIp().equals(MyInformation.getMyInformation().getIp()) && clientDetails.getPort() == MyInformation.getMyInformation().getPort())
                            continue;
                        try {
                            if (downloadQueueItem.getLastRetryClient().contains(clientDetails) && alreadyRequested == null) {
                                alreadyRequested = clientDetails;
                                continue;
                            }
                            String clientEndPoint = Utility.getRMIEndpoint(clientDetails.getIp(), clientDetails.getPort(),
                                    configManager.getValue(ConfigManager.CLIENT_BINDING_NAME));
                            IFileDownloaderClient client_stub = (IFileDownloaderClient) Naming.lookup(clientEndPoint);
                            long waittime = client_stub.getLoad(MyInformation.getBandwidth(), MyInformation.getMyInformation(), downloadQueueItem.getFilename());
                            // for failed cases, don't download from yourself.
                            if ((waittime < minSeenYet) &&
                                    (!clientDetails.toString().equals(MyInformation.getMyInformation().toString()))) {
                                chosenClientDetails = clientDetails;
                                minSeenYet = waittime;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                    if (minSeenYet == Integer.MAX_VALUE)
                        minSeenYet = 120;
                    minSeenYet = Math.max(120, minSeenYet); //default timeout 2 mins.
                    if (chosenClientDetails == null) {
                        if (alreadyRequested != null) {
                            chosenClientDetails = alreadyRequested;
                        } else {
                            System.out.println("No client is available. File Name: " + downloadQueueItem.getFilename());
                            continue;
                        }
                    }

                    //Using the chosen client build the rmi stub.
                    String clientEndPoint = Utility.getRMIEndpoint(chosenClientDetails.getIp(), chosenClientDetails.getPort(),
                            configManager.getValue(ConfigManager.CLIENT_BINDING_NAME));
                    IFileDownloaderClient client_stub = (IFileDownloaderClient) Naming.lookup(clientEndPoint);

                    // start socket for download.
                    ClientDetails recieverClientDetails = new ClientDetails(Utility.getIP(), this.port, MyInformation.getMyInformation().getClientId());
                    FileReceiverSocketThread receiverSocketThread = new FileReceiverSocketThread(recieverClientDetails,
                            downloadQueueItem.getFilename(), (int) minSeenYet * 3);
                    receiverSocketThread.start();
                    final boolean[] isCompleted = {true};
                    receiverSocketThread.setUncaughtExceptionHandler((t, e) -> isCompleted[0] = false);
                    // request for file send.
                    client_stub.requestFileSend(MyInformation.getBandwidth(), recieverClientDetails, downloadQueueItem.getFilename(), minSeenYet);
                    // wait for file to be received.
                    receiverSocketThread.join();

                    if (isCompleted[0] == false)
                        throw new Exception("Socket TImed Out!");
                    // verify checksum.
                    if (!client_stub.getCheckSum(downloadQueueItem.getFilename()).equals(FileHandler.calculateCheckSum(downloadQueueItem.getFilename()))) {
                        System.out.println(client_stub.getCheckSum(downloadQueueItem.getFilename()));
                        System.out.println(FileHandler.calculateCheckSum(downloadQueueItem.getFilename()));
                        throw new Exception("Checksum Failed!");
                    }

                    try {
                        // tell server you have a new file to share.
                        server_stub.updateList(MyInformation.getMyInformation(), new String[]{downloadQueueItem.getFilename()});
                    } catch (Exception ignored) {

                    }
                    System.out.println("Download Successful. Filename: " + downloadQueueItem.getFilename());
                }
            } catch (Exception e) {
                System.out.println("Thread" + threadid + ": " + e.getMessage());
                // If file already exists, don't download.
                if (FileHandler.doesFileExists(downloadQueueItem.getFilename())) {
                    //delete the file.
                    System.out.println("Deleting File.");
                    FileHandler.deleteFile(downloadQueueItem.getFilename());
                }
                // check if retry count is less than 3.
                if (downloadQueueItem.getRetryCount() < 3) {
                    Set<ClientDetails> lastRetryClient = new HashSet<>(downloadQueueItem.getLastRetryClient());
                    lastRetryClient.add(chosenClientDetails);
                    DownloadQueue.addDownloadRequestToQueue(new DownloadQueueItem(downloadQueueItem.getFilename(),
                            downloadQueueItem.getRetryCount() + 1, lastRetryClient));
                } else
                    System.out.println("Download Failed after 3 tries. File name: " + downloadQueueItem.getFilename());
            }
        }
    }
}
