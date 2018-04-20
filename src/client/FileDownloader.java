package client;

import model.ClientDetails;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


// takenFrom: https://stackoverflow.com/questions/9520911/java-sending-and-receiving-file-byte-over-sockets
public class FileDownloader {
    public void send(int port, String pathforthefiletosend) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;

        socket = serverSocket.accept();
        in = socket.getInputStream();

        out = new FileOutputStream(pathforthefiletosend);

        byte[] bytes = new byte[16 * 1024];

        int count;
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }

        out.close();
        in.close();
        socket.close();
        serverSocket.close();
    }

    public void download(String downloadHost, int port, String pathtostorethedownloadedfile) throws IOException {
        Socket socket = null;
        String host = downloadHost;

        socket = new Socket(host, port);

        File file = new File(pathtostorethedownloadedfile);
        // Get the size of the file
        long length = file.length();
        byte[] bytes = new byte[16 * 1024];
        InputStream in = new FileInputStream(file);
        OutputStream out = socket.getOutputStream();

        int count;
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }

        out.close();
        in.close();
        socket.close();
    }
}
