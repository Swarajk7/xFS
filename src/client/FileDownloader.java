package client;

import model.ClientDetails;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


// takenFrom: https://stackoverflow.com/questions/9520911/java-sending-and-receiving-file-byte-over-sockets
public class FileDownloader {
    public void send(String host, int port, String pathforthefiletosend,boolean shouldCorrupt) throws IOException {

        Socket socket = new Socket(host, port);

        File file = new File(pathforthefiletosend);
        // Get the size of the file
        long length = file.length();
        byte[] bytes = new byte[16 * 1024];
        InputStream in = new FileInputStream(file);
        OutputStream out = socket.getOutputStream();

        int count;
        while ((count = in.read(bytes)) > 0) {
            if (shouldCorrupt) bytes[0] = 1;
            out.write(bytes, 0, count);
        }

        out.close();
        in.close();
        socket.close();
    }

    public void download(int port, String pathtostorethedownloadedfile, int socketTimeOut) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(socketTimeOut*1000);
            socket = serverSocket.accept();
            socket.setSoTimeout(socketTimeOut*1000);
            in = socket.getInputStream();
            out = new FileOutputStream(pathtostorethedownloadedfile);
            byte[] bytes = new byte[16 * 1024];
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        }
    }
}
