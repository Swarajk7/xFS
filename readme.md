We have developed an xFS system which is a simple serverless file system. Main server just stores information about files available at each of the peers. When a client wants to download a file, it contacts the main server to get the list of peer servers which have the requested file. It contacts all the list of peer servers returned by the main server to understand the load on each of those server. It then chooses the best available peer server based on server load and latency information. Each peer server returns approximate time client would take to get the file from the peer server(approximate download time calculation is explained later). Each time a peer starts, it updates the main server with the list of files it has and keeps pinging the main server. If ping fails, client detects that the main server is down and when it is able to successfully ping the main server again, it updates the main server with the list of files it has. This takes care of main server going down. A thread in main server constantly checks the last ping from each of the peer server. If the last ping gap is more than a threshold value(mentioned in config file), then the server removes that peer from all the file details stored at the main server. This takes care of peer going down. When the peer which went down comes back, it will update the main server with info related to files it have. Client makes use of file checksum(MD5 hash of the file content) after downloading to ensure that the file hasn’t been corrupted. If all the peers associated with a file are down, then the client won’t be able to download the file. Similarly, if none of the peers have the file requested by the client, then also the client won’t be able to download the file. Design principles used include “log updates”(in the retry attempt we choose a peer which hasn’t been approached yet using logged updates) as well as “leave it to the client”(client decides what to do if a download attempt fails). Clients also uses timeouts to void download attempts and retry at other clients. Incase of checksum mismatch client will download the file from other available clients.

#### Choosing the peer server

Client chooses the peer server which gives least estimated time for download. Approximate estimated time for download considers latency, load at the server as well as bandwidth of the client and server. 

If free thread is available on the peer server side:
approx_estimated_time = latency + fileSize/min(client_bandwidth, peer_bandwidth)

If free thread isn’t available on the peer side:
We always keep track of the send jobs associated with each peer node. Using that info we calculate the approximate average time it will take for one of the threads to be free.

approx_estimated_time = approx_avg_time_for_thread_to_be_free + latency + 
fileSize/min(client_bandwidth, peer_bandwidth)

### How to build?
Within src folder. Run below command to build.
javac -cp . server/Server.java
javac -cp . client/Client.java

### How to run?
java server.Server serverPortNumber     (Ex: java server.Server 5005)
java client.Client clientPortNumber clientUniqueId          (Ex: java client.Client 5006 1)

Update config file with proper ip address and port number of the main server. Set the basepath with one folder in the machine [folder should exist]. Then create folder named clientid(say 1) in the basepath just mentioned and copy files to that folder. Latency information should also be updated in the config file. If no latency value is mentioned for pair of servers, then a default latency value will be used.
E.g. \usr\khada004\ should be the base path. In this folder, create a folder c1. Copy some files to c1 and then run client as java client.Client 6005 1. 

### Test Cases
1. Run Server. Run multiple clients(on different machines). Give a comma separated list in one file, which will download files from other clients.
2. Run server. Run multiple clients. Give one filename, which no client has. It will add it to download queue and eventually display file not found error.
3. Run server. Run 2 clients. In 1 client, try downloading a huge file. During download, shutdown the sender. Receiver should fail the download, delete half downloaded file and try downloading it from other available peers. 
4. Run server. Run 2 clients. In 1 client, try downloading a huge file. During download, shutdown the receiver client. Sender should detect the fail and ignore.
5. Run server. Run clients. Shutdown server and connect again. Server will be populated with new list. Then download flow should work as usual.
6. Run client. Stop client and wait for 20 seconds. Server should remove client from its memory. Again restart client. Server will repopulate the client information.
7. Change corrupt flag to true. And try the download flow. It should give checksum errors and download should fail.
8. Try downloading a 2GB or larger file, it should work just fine.
9. If server stops, client won’t be able to download any file.
10. For any kind of failure, client should try 3 times to download. Verify this by setting checksum corrupt flag in config to true. For testing this, first start a client which has the file with config file setting “corrupt” to true and then later start another client which has the file with config file setting “corrupt” set to false. The server for which config setting “corrupt” is set to true will corrupt one of the bytes while sending. 

### Negative Test Cases
1. Client only tells server about its files only after it downloads or at start of application. So If you copy/delete any file externally(outside download) to its folder, you need to restart the client, it won’t be automatically detected.
2. If receiver is shutdown while receiving a file, then partially download file will be their on the receiver side and it won’t be removed automatically.
