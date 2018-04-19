package common;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utility {
    public static String getRMIEndpoint(String ip, int port, String binding_name) {
        String rmi_end_point = "rmi://" + ip + ":" + port + "/" + binding_name;
        System.out.println("Your current RMI address : " + rmi_end_point);
        return rmi_end_point;
    }

    public static String getIP() throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Your current IP address : " + ip);
        return ip;
    }

    public static int parseAndGetPortNumber(String[] args) {
        //take port number as command line argument
        //System.out.println(args.length);
        if (args.length != 1) {
            System.out.println("Error.\nUsage java Server port_no");
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
}
