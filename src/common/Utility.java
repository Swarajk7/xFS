package common;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utility {
    public static String getRMIEndpoint(String ip, int port, String binding_name) {
        //System.out.println("Your current RMI address : " + rmi_end_point);
        return "rmi://" + ip + ":" + port + "/" + binding_name;
    }

    public static String getIP() throws UnknownHostException {
        //System.out.println("Your current IP address : " + ip);
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static int parseAndGetPortNumber(String[] args) {
        //take port number as command line argument
        //System.out.println(args.length);
        if (args.length < 1) {
            System.out.println("Error.\nUsage java Server port_no unique_client_id");
            System.exit(1);
        }
        int port = -1;
        try {
            //check if port is integer
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            System.out.println("Invalid Port Number.\nUsage java Client port_no");
            System.exit(1);
        }
        return port;
    }

    public static String parseAndGetClientId(String[] args) {
        //take port number as command line argument
        //System.out.println(args.length);
        if (args.length != 2) {
            System.out.println("Error.\nUsage java Server port_no unique_client_id");
            System.exit(1);
        }
        return args[1];
    }
}
