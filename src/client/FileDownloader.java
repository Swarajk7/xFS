package client;

import model.ClientDetails;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


// takenFrom: https://stackoverflow.com/questions/9520911/java-sending-and-receiving-file-byte-over-sockets
public class FileDownloader {
    public void send(String host, int port, String pathforthefiletosend) throws IOException {

        Socket socket = new Socket(host, port);

        File file = new File(pathforthefiletosend);
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

    public void download(int port, String pathtostorethedownloadedfile) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept();
             InputStream in = socket.getInputStream();
             OutputStream out = new FileOutputStream(pathtostorethedownloadedfile)) {
            /* todo: need to set timeout to the socket. */
            byte[] bytes = new byte[16 * 1024];

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
        } catch (IOException e) {
            throw e;
        }
    }
}
