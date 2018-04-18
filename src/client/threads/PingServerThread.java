package client.threads;

public class PingServerThread implements Runnable {
    public PingServerThread() {
        new Thread(this, "Client Side Thread for Ping Server!!").start();
    }

    @Override
    public void run() {
        boolean shouldUpdateServerWithListOfFiles = true;
        while (true) {
            try {
                // ping server

                // if ping is sucessful, server is up. :)
                if (shouldUpdateServerWithListOfFiles) {
                    // tell the server about your files by calling appropriate endpoint.

                    // if sucessful set shouldUpdateServerWithListOfFiles to false.
                    shouldUpdateServerWithListOfFiles = false;
                }
            } catch (Exception ex) {
                System.out.println("Server is Down.");
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
